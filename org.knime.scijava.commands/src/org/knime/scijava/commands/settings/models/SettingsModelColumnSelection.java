package org.knime.scijava.commands.settings.models;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class SettingsModelColumnSelection extends SettingsModelString {

    private DataTableSpec spec;

    public SettingsModelColumnSelection(final String configName,
            final String defaultValue) {
        super(configName, defaultValue);
    }

    public void setSpec(final DataTableSpec spec) {
        this.spec = spec;
    }

    public DataTableSpec getSpec() {
        return spec;
    };
}
