package org.knime.scijava.commands.settings.types;

import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.scijava.commands.settings.SettingsModelType;
import org.scijava.plugin.Plugin;

/**
 * SettingsModelType implementation for SettingsModelString.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = SettingsModelType.class)
public class SettingsModelStringType
        implements SettingsModelType<SettingsModelString, String> {

    @Override
    public SettingsModelString create(final String name,
            final String defaultValue) {
        if (defaultValue == null) {
            return new SettingsModelString(name, "");
        }
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
