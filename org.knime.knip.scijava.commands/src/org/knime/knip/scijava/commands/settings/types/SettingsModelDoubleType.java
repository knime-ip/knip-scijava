package org.knime.knip.scijava.commands.settings.types;

import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.knip.scijava.commands.settings.SettingsModelTypePlugin;
import org.scijava.plugin.Plugin;

/**
 * SettingsModelType implementation for SettingsModelDouble.
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = SettingsModelTypePlugin.class)
public class SettingsModelDoubleType implements
		SettingsModelTypePlugin<SettingsModelDouble, Double> {

	@Override
	public SettingsModelDouble create(String name, Double defaultValue) {
		return new SettingsModelDouble(name, defaultValue);
	}

	@Override
	public void setValue(SettingsModelDouble settingsModel, Double value) {
		settingsModel.setDoubleValue(value);
	}

	@Override
	public Double getValue(SettingsModelDouble settingsModel) {
		return settingsModel.getDoubleValue();
	}

	@Override
	public Class<SettingsModelDouble> getSettingsModelClass() {
		return SettingsModelDouble.class;
	}

	@Override
	public Class<Double> getValueClass() {
		return Double.class;
	}

}
