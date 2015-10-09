package org.knime.knip.scijava.commands.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

/**
 * Default implementation of NodeSettingsService.
 * 
 * @author Jonathan Hale
 */
@SuppressWarnings("rawtypes")
@Plugin(type = NodeSettingsService.class)
public class DefaultNodeSettingsService extends AbstractService implements
		NodeSettingsService {

	final Map<String, SettingsModel> m_settingsModels = new HashMap<String, SettingsModel>();

	@Parameter
	SettingsModelTypeService m_typeService;

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(final ModuleItem<?> moduleItem, final Object value) {
		final SettingsModel sm = m_settingsModels.get(moduleItem.getName());

		final SettingsModelType t = m_typeService.getSettingsModelTypeFor(sm);
		
		t.setValue(sm, value);
	}

	@Override
	public Object getValue(final ModuleItem<?> moduleItem) {
		final SettingsModel sm = m_settingsModels.get(moduleItem.getName());

		final SettingsModelType t = m_typeService.getSettingsModelTypeFor(sm);

		if (t != null) {
			return t.getValue(sm);
		}

		return null;
	}
	
	@Override
	public SettingsModel createSettingsModel(final ModuleItem<?> moduleItem) {
		SettingsModel sm = m_settingsModels.get(moduleItem.getName());
		
		if (sm != null) {
			// already exists, do not overwrite.
			return sm;
		}
		
		final SettingsModelType t = m_typeService.getSettingsModelTypeFor(moduleItem.getType());
		
		if (t == null) {
			return null;
		}
		
		sm = t.create(moduleItem.getName(), moduleItem.getMinimumValue());
		m_settingsModels.put(moduleItem.getName(), sm);
		
		return sm;
	}
	
	@Override
	public Collection<SettingsModel> createSettingsModels(
			final Iterable<ModuleItem<?>> moduleItems) {
		final ArrayList<SettingsModel> settingsModels = new ArrayList<SettingsModel>();
		
		for (final ModuleItem i : moduleItems) {
			SettingsModel sm = createSettingsModel(i);
			
			if (sm != null) {
				settingsModels.add(sm);
			}
		}
		return settingsModels;
	}

	@Override
	public Collection<SettingsModel> getSettingsModels() {
		return m_settingsModels.values();
	}

	@Override
	public boolean validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		for (final SettingsModel sm : m_settingsModels.values()) {
			sm.validateSettings(settings);
		}
		
		return true;
	}

	@Override
	public boolean loadSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		for (final SettingsModel sm : m_settingsModels.values()) {
			sm.loadSettingsFrom(settings);
		}
		
		return true;
	}

	@Override
	public boolean saveSettingsTo(final NodeSettingsWO settings) {
		for (final SettingsModel sm : m_settingsModels.values()) {
			sm.saveSettingsTo(settings);
		}
		
		return true;
	}

}
