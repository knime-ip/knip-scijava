package org.knime.knip.scijava.commands.settings.types;

import java.awt.Color;

import org.knime.core.node.defaultnodesettings.SettingsModelColor;
import org.knime.knip.scijava.commands.settings.SettingsModelTypePlugin;
import org.scijava.plugin.Plugin;

/**
 * SettingsModelType implementation for SettingsModelColor.
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = SettingsModelTypePlugin.class)
public class SettingsModelColorType implements
		SettingsModelTypePlugin<SettingsModelColor, Color> {

	@Override
	public SettingsModelColor create(String name, Color defaultValue) {
		return new SettingsModelColor(name, defaultValue);
	}

	@Override
	public void setValue(SettingsModelColor settingsModel, Color value) {
		settingsModel.setColorValue(value);
	}

	@Override
	public Color getValue(SettingsModelColor settingsModel) {
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
