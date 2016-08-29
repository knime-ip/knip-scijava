package org.knime.scijava.commands.settings.types;

import org.knime.core.node.defaultnodesettings.SettingsModelLong;
import org.knime.scijava.commands.settings.SettingsModelType;
import org.scijava.plugin.Plugin;

/**
 * SettingsModelType implementation for LongType inputs.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = SettingsModelType.class)
public class SettingsModelLongType
		implements SettingsModelType<SettingsModelLong, Long> {

	@Override
	public SettingsModelLong create(final String name,
			final Long defaultValue) {
		if (defaultValue == null) {
			return new SettingsModelLong(name, 0);
		}
		return new SettingsModelLong(name, defaultValue);
	}

	@Override
	public void setValue(final SettingsModelLong settingsModel,
			final Long value) throws ClassCastException {
		settingsModel.setLongValue(value);
	}

	@Override
	public Long getValue(final SettingsModelLong settingsModel) {
		return settingsModel.getLongValue();
	}

	@Override
	public Class<SettingsModelLong> getSettingsModelClass() {
		return SettingsModelLong.class;
	}

	@Override
	public Class<Long> getValueClass() {
		return Long.class;
	}

}
