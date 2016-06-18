package org.knime.scijava.commands.settings.types;

import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.scijava.commands.settings.SettingsModelTypePlugin;
import org.scijava.plugin.Plugin;

/**
 * SettingsModelType implementation for SettingsModelDouble.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = SettingsModelTypePlugin.class)
public class SettingsModelDoubleType
		implements SettingsModelTypePlugin<SettingsModelDouble, Double> {

	@Override
	public SettingsModelDouble create(final String name,
			final Double defaultValue) {
		if (defaultValue == null) {
			return new SettingsModelDouble(name, 0.0d);
		}
		return new SettingsModelDouble(name, defaultValue);
	}

	@Override
	public void setValue(final SettingsModelDouble settingsModel,
			final Double value) {
		settingsModel.setDoubleValue(value);
	}

	@Override
	public Double getValue(final SettingsModelDouble settingsModel) {
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
