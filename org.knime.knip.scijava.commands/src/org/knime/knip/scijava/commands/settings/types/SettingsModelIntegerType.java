package org.knime.knip.scijava.commands.settings.types;

import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.knip.scijava.commands.settings.SettingsModelTypePlugin;
import org.scijava.plugin.Plugin;

/**
 * SettingsModelType implementation for SettingsModelInteger.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = SettingsModelTypePlugin.class)
public class SettingsModelIntegerType
		implements SettingsModelTypePlugin<SettingsModelInteger, Integer> {

	@Override
	public SettingsModelInteger create(final String name,
			Integer defaultValue) {
		if (defaultValue == null) {
			defaultValue = new Integer(0);
		}

		return new SettingsModelInteger(name, defaultValue);
	}

	@Override
	public void setValue(final SettingsModelInteger settingsModel,
			final Integer value) throws ClassCastException {
		settingsModel.setIntValue(value);
	}

	@Override
	public Integer getValue(final SettingsModelInteger settingsModel) {
		return settingsModel.getIntValue();
	}

	@Override
	public Class<SettingsModelInteger> getSettingsModelClass() {
		return SettingsModelInteger.class;
	}

	@Override
	public Class<Integer> getValueClass() {
		return Integer.class;
	}

}
