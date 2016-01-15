package org.knime.scijava.commands.settings;

import java.util.HashMap;
import java.util.Map;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.scijava.plugin.AbstractSingletonService;
import org.scijava.plugin.Plugin;

/**
 * Straight forward, rather ineffective default implementation of
 * SettingsModelTypeService.
 *
 * Ineffective because {@link #getSettingsModelTypeFor(SettingsModel)} uses
 * linear search to find a matching SettingsModelType.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@SuppressWarnings("rawtypes")
@Plugin(type = SettingsModelTypeService.class)
public class DefaultSettingsModelTypeService
		extends AbstractSingletonService<SettingsModelTypePlugin>
		implements SettingsModelTypeService {

	Map<Class<? extends SettingsModel>, SettingsModelTypePlugin> m_pluginsByModel = new HashMap<>();
	Map<Class<?>, SettingsModelTypePlugin> m_pluginsByValue = new HashMap<>();

	@Override
	public Class<SettingsModelTypePlugin> getPluginType() {
		return SettingsModelTypePlugin.class;
	}

	@Override
	public SettingsModelType getSettingsModelTypeFor(
			final SettingsModel settingsModel) {

		final SettingsModelTypePlugin plugin = m_pluginsByModel
				.get(settingsModel.getClass());
		if (plugin != null) {
			return plugin;
		}

		for (final SettingsModelTypePlugin p : getInstances()) {
			if (p.getSettingsModelClass().isInstance(settingsModel)) {
				m_pluginsByModel.put(settingsModel.getClass(), p);
				return p;
			}
		}
		// nothing found
		return null;
	}

	@Override
	public SettingsModelType getSettingsModelTypeFor(final Class<?> value) {

		// check cache
		SettingsModelTypePlugin plugin = m_pluginsByValue.get(value);
		if (plugin != null) {
			return plugin;
		}
		// search
		for (final SettingsModelTypePlugin<?, ?> p : getInstances()) {
			if (p.getValueClass().isAssignableFrom(value)) {
				m_pluginsByValue.put(value, p);
				return p;
			}
		}

		// nothing found
		return null;
	}

}
