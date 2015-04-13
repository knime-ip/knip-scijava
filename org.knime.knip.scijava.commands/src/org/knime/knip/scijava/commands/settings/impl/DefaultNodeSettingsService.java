package org.knime.knip.scijava.commands.settings.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.knip.scijava.commands.settings.NodeSettingsService;
import org.knime.knip.scijava.commands.settings.SettingsModelType;
import org.knime.knip.scijava.commands.settings.SettingsModelTypeService;
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

	Map<String, SettingsModel> m_settingsModels = new HashMap<String, SettingsModel>();

	@Parameter
	SettingsModelTypeService m_typeService;

	@Override
	public SettingsModel getSettingsModel(ModuleItem<?> moduleItem) {
		return m_settingsModels.get(moduleItem.getName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(ModuleItem<?> moduleItem, Object value) {
		SettingsModel sm = m_settingsModels.get(moduleItem.getName());

		SettingsModelType t = m_typeService.getSettingsModelTypeFor(sm);
		
		t.setValue(sm, value);
	}

	@Override
	public Object getValue(ModuleItem<?> moduleItem) {
		SettingsModel sm = m_settingsModels.get(moduleItem.getName());

		SettingsModelType t = m_typeService.getSettingsModelTypeFor(sm);

		if (t != null) {
			return t.getValue(sm);
		}

		return null;
	}
	
	@Override
	public SettingsModel createSettingsModel(ModuleItem<?> moduleItem) {
		SettingsModel sm = m_settingsModels.get(moduleItem.getName());
		
		if (sm != null) {
			// already exists, do not overwrite.
			return sm;
		}
		
		SettingsModelType t = m_typeService.getSettingsModelTypeFor(moduleItem.getType());
		
		if (t == null) {
			return null;
		}
		
		sm = t.create(moduleItem.getName(), moduleItem.getMinimumValue());
		m_settingsModels.put(moduleItem.getName(), sm);
		
		return sm;
	}
	
	@Override
	public Collection<SettingsModel> createSettingsModels(
			Iterable<ModuleItem<?>> moduleItems) {
		ArrayList<SettingsModel> settingsModels = new ArrayList<SettingsModel>();
		
		for (ModuleItem i : moduleItems) {
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
	public boolean validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		for (SettingsModel sm : m_settingsModels.values()) {
			sm.validateSettings(settings);
		}
		
		return true;
	}

	@Override
	public boolean loadSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		for (SettingsModel sm : m_settingsModels.values()) {
			sm.loadSettingsFrom(settings);
		}
		
		return true;
	}

	@Override
	public boolean saveSettingsTo(NodeSettingsWO settings) {
		for (SettingsModel sm : m_settingsModels.values()) {
			sm.saveSettingsTo(settings);
		}
		
		return true;
	}

}
