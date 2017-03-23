package org.knime.scijava.commands.exp.template.multimap;

import java.util.List;

import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.util.Pair;
import org.knime.scijava.commands.dialog.DialogService;
import org.knime.scijava.commands.settings.models.SettingsModelColumnSelection;
import org.knime.scijava.commands.widget.SettingsModelWidgetModel;
import org.scijava.Context;
import org.scijava.module.ModuleInfo;
import org.scijava.plugin.Parameter;

/**
 * Dialog for the Scripting Node.
 *
 * @author <a href="mailto:jonathan.hale@uni-konstanz.de">Jonathan Hale</a>
 * @author <a href="mailto:dietzc85@googlemail.com">Christian Dietz</a>
 *
 * @see SciJavaScriptingNodeModel
 * @see SciJavaScriptingNodeFactory
 */
public class SciJavaMultiMapNodeDialog extends NodeDialogPane {

    @Parameter
    private DialogService ds;

    private final List<SettingsModelWidgetModel> settingsModels;

    public SciJavaMultiMapNodeDialog(final Context ctx, final ModuleInfo info) {
        ctx.inject(this);
        final Pair<JPanel, List<SettingsModelWidgetModel>> dialog = ds
                .dialogPanel(info);
        addTab("Configure", dialog.getFirst());
        settingsModels = dialog.getSecond();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings)
            throws InvalidSettingsException {
        for (final SettingsModelWidgetModel model : settingsModels) {
            model.getSettingsModel().saveSettingsTo(settings);
        }
    }

    @Override
    protected void loadSettingsFrom(final NodeSettingsRO settings,
            final DataTableSpec[] specs) throws NotConfigurableException {

        try {
            for (final SettingsModelWidgetModel widget : settingsModels) {
                final SettingsModel model = widget.getSettingsModel();
                model.loadSettingsFrom(settings);

                if (model instanceof SettingsModelColumnSelection) {
                    ((SettingsModelColumnSelection) model).setSpec(specs[0]);

                }
                widget.getPanel().refresh();
            }

        } catch (final InvalidSettingsException e) {
            throw new NotConfigurableException("Can't create dialog : " + e);
        }
    }

}
