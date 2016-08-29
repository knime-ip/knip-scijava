package org.knime.scijava.commands.dialog;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.knime.scijava.commands.widget.SettingsModelWidgetModel;
import org.scijava.ui.swing.widget.SwingInputPanel;
import org.scijava.widget.InputWidget;

public class KNIMEInputPanel extends SwingInputPanel {

    public List<SettingsModelWidgetModel> getWidgets() {
        final List<SettingsModelWidgetModel> models = new ArrayList<SettingsModelWidgetModel>();

        for (final InputWidget<?, JPanel> widget : widgets.values()) {
            final SettingsModelWidgetModel widgetModel = ((SettingsModelWidgetModel) widget
                    .get());
            models.add(widgetModel);
        }

        return models;
    }

}
