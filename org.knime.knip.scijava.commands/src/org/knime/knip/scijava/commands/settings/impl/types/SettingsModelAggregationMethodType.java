package org.knime.knip.scijava.commands.settings.impl.types;

import org.knime.base.data.aggregation.AggregationMethod;
import org.knime.base.data.aggregation.dialogutil.SettingsModelAggregationMethod;
import org.knime.knip.scijava.commands.settings.SettingsModelTypePlugin;
import org.scijava.plugin.Plugin;

/**
 * SettingsModelType implementation for SettingsModelAggregationMethod.
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = SettingsModelTypePlugin.class)
public class SettingsModelAggregationMethodType
		implements
		SettingsModelTypePlugin<SettingsModelAggregationMethod, AggregationMethod> {

	@Override
	public SettingsModelAggregationMethod create(String name,
			AggregationMethod defaultValue) {
		return new SettingsModelAggregationMethod(name, defaultValue);
	}

	@Override
	public void setValue(SettingsModelAggregationMethod settingsModel,
			AggregationMethod value) {
		settingsModel.setAggregationMethod(value);
	}

	@Override
	public AggregationMethod getValue(
			SettingsModelAggregationMethod settingsModel) {
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
