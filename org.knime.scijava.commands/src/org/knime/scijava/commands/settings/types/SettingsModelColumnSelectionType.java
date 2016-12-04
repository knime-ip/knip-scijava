package org.knime.scijava.commands.settings.types;

import org.knime.scijava.commands.settings.SettingsModelType;
import org.knime.scijava.commands.settings.models.SettingsModelColumnSelection;
import org.scijava.plugin.Plugin;

/**
 * SettingsModelType implementation for SettingsModelColumnSelection.
 *
 * @author Christian Dietz, University of Konstanz
 */
@Plugin(type = SettingsModelType.class)
public class SettingsModelColumnSelectionType
        implements SettingsModelType<SettingsModelColumnSelection, String> {

    @Override
    public SettingsModelColumnSelection create(final String name,
            final String defaultValue) {
        return new SettingsModelColumnSelection(name, defaultValue);
    }

    @Override
    public void setValue(final SettingsModelColumnSelection settingsModel,
            final String value) {
        settingsModel.setStringValue(value);
    }

    @Override
    public String getValue(final SettingsModelColumnSelection settingsModel) {
        return settingsModel.getStringValue();
    }

    @Override
    public Class<SettingsModelColumnSelection> getSettingsModelClass() {
        return SettingsModelColumnSelection.class;
    }

    @Override
    public Class<String> getValueClass() {
        return String.class;
    }

}
