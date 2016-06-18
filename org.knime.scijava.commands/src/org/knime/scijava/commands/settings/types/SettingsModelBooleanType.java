package org.knime.scijava.commands.settings.types;

import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.scijava.commands.settings.SettingsModelTypePlugin;
import org.scijava.plugin.Plugin;

/**
 * SettingsModelType implementation for SettingsModelBoolean.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = SettingsModelTypePlugin.class)
public class SettingsModelBooleanType
		implements SettingsModelTypePlugin<SettingsModelBoolean, Boolean> {

	@Override
	public SettingsModelBoolean create(final String name,
			final Boolean defaultValue) {
		if (defaultValue == null) {
			return new SettingsModelBoolean(name, false);
		}
		return new SettingsModelBoolean(name, defaultValue);
	}

	@Override
	public void setValue(final SettingsModelBoolean settingsModel,
			final Boolean value) {
		settingsModel.setBooleanValue(value);
	}

	@Override
	public Boolean getValue(final SettingsModelBoolean settingsModel) {
		return settingsModel.getBooleanValue();
	}

	@Override
	public Class<SettingsModelBoolean> getSettingsModelClass() {
		return SettingsModelBoolean.class;
	}

	@Override
	public Class<Boolean> getValueClass() {
		return Boolean.class;
	}

}
