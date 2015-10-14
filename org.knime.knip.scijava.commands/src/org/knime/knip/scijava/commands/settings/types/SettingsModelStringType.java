package org.knime.knip.scijava.commands.settings.types;

import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.knip.scijava.commands.settings.SettingsModelTypePlugin;
import org.scijava.plugin.Plugin;

/**
 * SettingsModelType implementation for SettingsModelString.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = SettingsModelTypePlugin.class)
public class SettingsModelStringType
		implements SettingsModelTypePlugin<SettingsModelString, String> {

	@Override
	public SettingsModelString create(final String name,
			final String defaultValue) {
		return new SettingsModelString(name, defaultValue);
	}

	@Override
	public void setValue(final SettingsModelString settingsModel,
			final String value) throws ClassCastException {
		settingsModel.setStringValue(value);
	}

	@Override
	public String getValue(final SettingsModelString settingsModel) {
		return settingsModel.getStringValue();
	}

	@Override
	public Class<SettingsModelString> getSettingsModelClass() {
		return SettingsModelString.class;
	}

	@Override
	public Class<String> getValueClass() {
		return String.class;
	}

}
