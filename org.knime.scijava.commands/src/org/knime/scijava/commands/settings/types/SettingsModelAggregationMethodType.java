package org.knime.scijava.commands.settings.types;

import org.knime.base.data.aggregation.AggregationMethod;
import org.knime.base.data.aggregation.dialogutil.SettingsModelAggregationMethod;
import org.knime.scijava.commands.settings.SettingsModelTypePlugin;
import org.scijava.plugin.Plugin;

/**
 * SettingsModelType implementation for SettingsModelAggregationMethod.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = SettingsModelTypePlugin.class)
public class SettingsModelAggregationMethodType implements
		SettingsModelTypePlugin<SettingsModelAggregationMethod, AggregationMethod> {

	@Override
	public SettingsModelAggregationMethod create(final String name,
			final AggregationMethod defaultValue) {
		return new SettingsModelAggregationMethod(name, defaultValue);
	}

	@Override
	public void setValue(final SettingsModelAggregationMethod settingsModel,
			final AggregationMethod value) {
		settingsModel.setAggregationMethod(value);
	}

	@Override
	public AggregationMethod getValue(
			final SettingsModelAggregationMethod settingsModel) {
		return settingsModel.getAggregationMethod();
	}

	@Override
	public Class<SettingsModelAggregationMethod> getSettingsModelClass() {
		return SettingsModelAggregationMethod.class;
	}

	@Override
	public Class<AggregationMethod> getValueClass() {
		return AggregationMethod.class;
	}

}
