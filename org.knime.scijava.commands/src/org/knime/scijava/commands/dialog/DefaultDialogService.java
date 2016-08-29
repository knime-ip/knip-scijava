package org.knime.scijava.commands.dialog;

import java.util.List;

import javax.swing.JPanel;

import org.knime.core.util.Pair;
import org.knime.scijava.commands.MultiOutputListener;
import org.knime.scijava.commands.widget.SettingsModelWidgetModel;
import org.scijava.module.Module;
import org.scijava.module.ModuleException;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.module.ModuleService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.ui.swing.widget.SwingInputHarvester;
import org.scijava.widget.InputPanel;
import org.scijava.widget.InputWidget;
import org.scijava.widget.WidgetModel;
import org.scijava.widget.WidgetService;

@Plugin(type = DialogService.class)
public class DefaultDialogService extends AbstractService
        implements DialogService {

    @Parameter
    private WidgetService ws;

    @Parameter
    private ModuleService ms;

    @Override
    public Pair<JPanel, List<SettingsModelWidgetModel>> dialogPanel(
            final Module module) {

        final SwingInputHarvester builder = new SwingInputHarvester() {
            @SuppressWarnings("unchecked")
            @Override
            public void buildPanel(final InputPanel<JPanel, JPanel> inputPanel,
                    final Module module) throws ModuleException {

                // automatically resolve special fields
                // FIXME can be extended using plugin mechanism?
                for (final ModuleItem<?> item : module.getInfo().inputs()) {
                    if (MultiOutputListener.class
                            .isAssignableFrom(item.getType())) {
                        module.setResolved(item.getName(), true);
                    }
                }

                // build input panel
                super.buildPanel(inputPanel, module);

                // build output panel
                // FIXME: Different Tab?
                for (final ModuleItem<?> item : module.getInfo().outputs()) {
                    final WidgetModel model = ws.createModel(inputPanel, module,
                            item, null);
                    inputPanel.addWidget(
                            (InputWidget<?, JPanel>) ws.create(model));
                }

            }
        };
        builder.setContext(getContext());

        final KNIMEInputPanel panel = new KNIMEInputPanel();
        try {
            builder.buildPanel(panel, module);
        } catch (final ModuleException e) {
            // FIXME Error Handling / Logging
            e.printStackTrace();
        }

        return new Pair<JPanel, List<SettingsModelWidgetModel>>(
                panel.getComponent(), panel.getWidgets());
    }

    @Override
    public Pair<JPanel, List<SettingsModelWidgetModel>> dialogPanel(
            final ModuleInfo info) {
        return dialogPanel(ms.createModule(info));
    }
}
