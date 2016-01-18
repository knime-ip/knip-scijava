package org.knime.scijava.commands.settings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.service.AbstractService;

/**
 * Default implementation of NodeSettingsService.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractDefaultSettingsService extends AbstractService
		implements NodeSettingsService {

	private WeakReference<Map<String, SettingsModel>> m_settingsModels = new WeakReference<>(
			null);

	@Parameter
	SettingsModelTypeService m_typeService;

	@Override
	public void setSettingsModels(
			final Map<String, SettingsModel> settingsModels) {
		m_settingsModels = new WeakReference<>(settingsModels);
	}

	/*
	 * @return Map referenced by m_settingsModels or empty Map. Never
	 * <code>null</code>
	 */
	private Map<String, SettingsModel> getSafeSettingsModelsMap() {
		if (m_settingsModels.get() == null) {
			return Collections.emptyMap();
		}

		return m_settingsModels.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(final ModuleItem<?> moduleItem, final Object value) {
		final SettingsModel sm = getSafeSettingsModelsMap()
				.get(moduleItem.getName());
		if (sm == null) {
			return;
		}

		final SettingsModelType t = m_typeService.getSettingsModelTypeFor(sm);

		t.setValue(sm, value);
	}

	@Override
	public Object getValue(final ModuleItem<?> moduleItem) {
		final SettingsModel sm = getSafeSettingsModelsMap()
				.get(moduleItem.getName());
		if (sm == null) {
			return null;
		}

		final SettingsModelType t = m_typeService.getSettingsModelTypeFor(sm);
		if (t != null) {
			return t.getValue(sm);
		}

		return null;
	}

	private SettingsModel createSettingsModel(final ModuleItem<?> moduleItem) {
		SettingsModel sm;

		final SettingsModelType t = m_typeService
				.getSettingsModelTypeFor(moduleItem.getType());
		if (t != null) {
			sm = t.create(moduleItem.getName(), moduleItem.getMinimumValue());
		} else {
			sm = new SettingsModelColumnName(moduleItem.getName(), "");
		}
		return sm;
	}

	@Override
	public SettingsModel createAndAddSettingsModel(
			final ModuleItem<?> moduleItem) {
		SettingsModel sm = getSafeSettingsModelsMap().get(moduleItem.getName());
		if (sm != null) {
			return sm; // already exists, do not overwrite.
		}
		sm = createSettingsModel(moduleItem);
		addSettingsModel(moduleItem.getName(), sm);
		return sm;
	}

	@Override
	public Collection<SettingsModel> createAndAddSettingsModels(
			final Iterable<ModuleItem<?>> moduleItems) {
		final ArrayList<SettingsModel> settingsModels = new ArrayList<>();
		moduleItems.forEach(
				item -> settingsModels.add(createAndAddSettingsModel(item)));
		return settingsModels;
	}

	@Override
	public boolean validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		for (final SettingsModel sm : getSafeSettingsModelsMap().values()) {
			sm.validateSettings(settings);
		}

		return true;
	}

	@Override
	public boolean loadSettingsFrom(final NodeSettingsRO settings,
			final boolean tolerant) throws InvalidSettingsException {
		for (final SettingsModel sm : getSafeSettingsModelsMap().values()) {
			try {
				sm.loadSettingsFrom(settings);
			} catch (final InvalidSettingsException e) {
				if (!tolerant) {
					throw e;
				}
			}
		}

		return true;
	}

	@Override
	public boolean saveSettingsTo(final NodeSettingsWO settings) {
		for (final SettingsModel sm : getSafeSettingsModelsMap().values()) {
			sm.saveSettingsTo(settings);
		}
		return true;
	}

	@Override
	public Map<String, SettingsModel> getSettingsModels() {
		return m_settingsModels.get();
	}

	private void addSettingsModel(final String name, final SettingsModel sm) {
		if (m_settingsModels.get() != null) {
			m_settingsModels.get().put(name, sm);
		}
	}

	@Override
	public void clear() {
		getSafeSettingsModelsMap().clear();
	}
}
