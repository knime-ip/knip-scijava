package org.knime.scijava.commands.settings;

import java.util.HashMap;
import java.util.Map;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.scijava.commands.MultiOutputListener;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.service.Service;

/**
 * Interface for services which handle SettingsModels.
 *
 * @author Christian Dietz, University of Konstanz
 */
public interface NodeSettingsService extends Service {

    /**
     * Get all settings models for a given module.
     *
     * @param module
     *            the module to get {@link SettingsModels} for.
     * @return Map of settings models with module item names as keys.
     */
    default Map<String, SettingsModel> getSettingsModels(
            final ModuleInfo module) {
        final Map<String, SettingsModel> models = new HashMap<>();

        for (final ModuleItem<?> item : module.inputs()) {
            // FIXME make extensible?
            if (MultiOutputListener.class.isAssignableFrom(item.getType())) {
                continue;
            }
            models.put(item.getName(), createSettingsModel(item));
        }

        for (final ModuleItem<?> item : module.outputs()) {
            models.put(item.getName(), createSettingsModel(item));
        }

        return models;
    }

    /**
     * Create settings model for a given module item.
     *
     * @param moduleItem
     *            the module item to create a settings model for.
     * @return The created {@link SettingsModel}
     */
    SettingsModel createSettingsModel(final ModuleItem<?> moduleItem);

    /**
     * Dynamically get the value of a {@link SettingsModel}.
     *
     * @param model
     *            the model to get the value of.
     * @return the value
     */
    Object getValue(final SettingsModel model);

    /**
     * Dynamically set the value of a {@link SettingsModel}.
     *
     * @param settingsModel
     *            model to set the value of.
     * @param value
     *            Value to set to
     */
    void setValue(final SettingsModel settingsModel, final Object value);
}
