package org.knime.knip.scijava.commands.settings.impl;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.knip.scijava.commands.settings.SettingsModelType;
import org.knime.knip.scijava.commands.settings.SettingsModelTypePlugin;
import org.knime.knip.scijava.commands.settings.SettingsModelTypeService;
import org.scijava.plugin.AbstractSingletonService;
import org.scijava.plugin.Plugin;

/**
 * Straight forward, rather ineffective default implementation of
 * SettingsModelTypeService.
 *
 * Ineffective because {@link #getSettingsModelTypeFor(SettingsModel)}
 * uses linear search to find a matching SettingsModelType.
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
@SuppressWarnings("rawtypes")
@Plugin(type = SettingsModelTypeService.class)
public class DefaultSettingsModelTypeService extends
		AbstractSingletonService<SettingsModelTypePlugin> implements
		SettingsModelTypeService {

	@Override
	public Class<SettingsModelTypePlugin> getPluginType() {
		return SettingsModelTypePlugin.class;
	}

	@Override
	public SettingsModelType getSettingsModelTypeFor(SettingsModel settingsModel) {
		for (SettingsModelTypePlugin p : getInstances()) {
			if (p.getSettingsModelClass().isInstance(settingsModel)) {
				return p;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SettingsModelType getSettingsModelTypeFor(Class<?> value) {
		for (SettingsModelTypePlugin p : getInstances()) {
			if (p.getValueClass().isAssignableFrom(value)) {
				return p;
			}
		}
		
		return null;
	}

}
