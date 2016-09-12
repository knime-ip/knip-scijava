package org.knime.scijava.commands.settings.models;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * SettingsModel for columns.
 *
 * @author Christian Dietz, University of Konstanz
 */
public class SettingsModelColumnSelection extends SettingsModelString {

    private DataTableSpec spec;

    /**
     * Constructor.
     *
     * @param configName
     *            Name of the setting
     * @param defaultValue
     *            Default value
     */
    public SettingsModelColumnSelection(final String configName,
            final String defaultValue) {
        super(configName, defaultValue);
    }

    /**
     * Set data table spec.
     *
     * @param spec
     *            the spec to set
     */
    public void setSpec(final DataTableSpec spec) {
        this.spec = spec;
    }

    /**
     * Get the contained DataTableSpec.
     *
     * @return the spec
     */
    public DataTableSpec getSpec() {
        return spec;
    };
}
