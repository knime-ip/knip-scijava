package org.knime.scijava.commands.settings;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.SingletonService;

/**
 * Interface for SettingsModelType functionality.
 *
 * <p>
 * SettingsModelTypeService plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link SettingsModelTypeService}.class.
 * </p>
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@SuppressWarnings("rawtypes")
public interface SettingsModelTypeService
        extends SingletonService<SettingsModelType> {

    /**
     * Get a SettingModelType which can set and get values from the specified
     * SettingsModel.
     *
     * @param settingsModel
     *            SettingsModel to find a SettingsModelType for.
     * @return a SettingsModelType which can set/get values from settingsModel
     *         or null if none could be found or <code>settingsModel</code> was
     *         <code>null</code>.
     */
    SettingsModelType getSettingsModelTypeFor(
            final SettingsModel settingsModel);

    /**
     * Get the {@link SettingsModelType} matching the given {@link ModuleItem}.
     *
     * @param item
     *            The item to get the settings model type for
     * @return the matching settings model type
     */
    SettingsModelType getSettingsModelTypeFor(final ModuleItem<?> item);

    /**
     * Dynamically get the value of a {@link SettingsModel}.
     *
     * @param model
     *            The settings model
     * @return value contained in the {@link SettingsModel}
     */
    Object getValueFrom(final SettingsModel model);
}
