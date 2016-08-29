package org.knime.scijava.commands.settings.types;

import java.awt.Color;

import org.knime.core.node.defaultnodesettings.SettingsModelColor;
import org.knime.scijava.commands.settings.SettingsModelType;
import org.scijava.plugin.Plugin;

/**
 * SettingsModelType implementation for SettingsModelColor.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = SettingsModelType.class)
public class SettingsModelColorType
		implements SettingsModelType<SettingsModelColor, Color> {

	@Override
	public SettingsModelColor create(final String name,
			final Color defaultValue) {
		if (defaultValue == null) {
			return new SettingsModelColor(name, Color.RED);
		}
		return new SettingsModelColor(name, defaultValue);
	}

	@Override
	public void setValue(final SettingsModelColor settingsModel,
			final Color value) {
		settingsModel.setColorValue(value);
	}

	@Override
	public Color getValue(final SettingsModelColor settingsModel) {
		return settingsModel.getColorValue();
	}

	@Override
	public Class<SettingsModelColor> getSettingsModelClass() {
		return SettingsModelColor.class;
	}

	@Override
	public Class<Color> getValueClass() {
		return Color.class;
	}

}
