package org.knime.scijava.commands.settings;

import java.util.HashMap;
import java.util.Map;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.scijava.commands.MultiOutputListener;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.service.Service;

public interface NodeSettingsService extends Service {

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

    SettingsModel createSettingsModel(final ModuleItem<?> moduleItem);

    Object getValue(final SettingsModel model);

    void setValue(SettingsModel settingsModel, Object value);

}
