package org.knime.scijava.commands.widget;

import java.util.List;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.scijava.commands.settings.NodeSettingsService;
import org.scijava.Context;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.widget.DefaultWidgetModel;
import org.scijava.widget.InputPanel;

/**
 * Widget model which stores its data in a settings model.
 *
 * @author Christian Dietz, University of Konstanz
 */
public class SettingsModelWidgetModel extends DefaultWidgetModel {

    @Parameter
    private NodeSettingsService settingsService;

    private final SettingsModel settingsModel;

    /**
     * Constructor for generic input items. The used SettingsModel will be
     * created by a {@link NodeSettingsService}.
     *
     * @see DialogInputWidgetModel
     * @param context
     *            Context for the model
     * @param inputPanel
     *            the panel
     * @param module
     *            the module
     * @param item
     *            the module item
     * @param objectPool
     *            the ObejctPool
     */
    public SettingsModelWidgetModel(final Context context,
            final InputPanel<?, ?> inputPanel, final Module module,
            final ModuleItem<?> item, final List<?> objectPool) {
        super(context, inputPanel, module, item, objectPool);

        // FIXME we should have a base64 encoded fall back SettinsModel for
        // things we can not directly map from scijava to KNIME (easy wa)
        settingsModel = settingsService.createSettingsModel(item);
    }

    @Override
    public Object getValue() {
        return settingsService.getValue(settingsModel);
    }

    @Override
    public void setValue(final Object value) {
        if (settingsModel != null) {
            settingsService.setValue(settingsModel, value);
        }
    }

    public SettingsModel getSettingsModel() {
        return settingsModel;
    }
}
