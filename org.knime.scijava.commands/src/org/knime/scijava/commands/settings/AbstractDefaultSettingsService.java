package org.knime.scijava.commands.settings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.scijava.commands.KNIMESciJavaConstants;
import org.scijava.module.Module;
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

	/*
	 * @return Map referenced by m_settingsModels or empty Map. Never
	 * <code>null</code>
	 */
	protected Map<String, SettingsModel> getSafeSettingsModelsMap() {
		if (m_settingsModels.get() == null) {
			m_settingsModels = new WeakReference<>(new HashMap<>());
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

	private SettingsModel createSettingsModel(final ModuleItem<?> moduleItem,
			Module module, final boolean forceColSelec) {
		SettingsModel sm;

		// check for if columnselection is forced
		if (forceColSelec) {
			sm = createColSelectModel(moduleItem);
		} else {
			final SettingsModelType t = m_typeService
					.getSettingsModelTypeFor(moduleItem.getType());
			if (t != null) {
				// get default value
				Object value = moduleItem.getValue(module);
				if (value != null) {
					sm = t.create(moduleItem.getName(), value);
					// set to default value
					t.setValue(sm, moduleItem.getValue(module));
				} else {
					sm = t.create(moduleItem.getName(),
							moduleItem.getMinimumValue());
				}
			} else {
				// can't create a SettingsModel for this type, we will try to
				// create a column selection Widget.
				sm = createColSelectModel(moduleItem);
			}
		}
		return sm;
	}

	private SettingsModel createColSelectModel(final ModuleItem<?> moduleItem) {
		return new SettingsModelString(moduleItem.getName(), "");
	}

	@Override
	public SettingsModel createAndAddSettingsModel(
			final ModuleItem<?> moduleItem, Module module,
			final boolean forceColumnSelection) {
		SettingsModel sm = getSafeSettingsModelsMap().get(moduleItem.getName());
		if (sm != null) {
			return sm; // already exists, do not overwrite.
		}

		sm = createSettingsModel(moduleItem, module, forceColumnSelection);
		getSafeSettingsModelsMap().put(moduleItem.getName(), sm);
		return sm;
	}

	@Override
	public List<SettingsModel> createAndAddSettingsModels(
			final Iterable<ModuleItem<?>> moduleItems, Module module) {
		final ArrayList<SettingsModel> settingsModels = new ArrayList<>();
		moduleItems.forEach(item -> {
			final boolean force = "true".equals(
					item.get(KNIMESciJavaConstants.COLUMN_SELECT_KEY));
			settingsModels.add(createAndAddSettingsModel(item, module, force));
		});

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
		final Collection<SettingsModel> values = getSafeSettingsModelsMap()
				.values();
		for (final SettingsModel sm : values) {
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
		final Collection<SettingsModel> values = getSafeSettingsModelsMap()
				.values();
		for (final SettingsModel sm : values) {
			sm.saveSettingsTo(settings);
		}
		return true;
	}

	@Override
	public void clear() {
		getSafeSettingsModelsMap().clear();
	}

	@Override
	public void removeSettingsModel(final ModuleItem<?> moduleItem) {
		getSafeSettingsModelsMap().remove(moduleItem.getName());
	}
}
