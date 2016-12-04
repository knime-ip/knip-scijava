package org.knime.scijava.commands.dialog;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.knime.scijava.commands.widget.SettingsModelWidgetModel;
import org.scijava.ui.swing.widget.SwingInputPanel;
import org.scijava.widget.InputWidget;

/**
 * {@link SwingInputPanel} which allows retrieval of used
 * {@link SettingsModelWidgetModel} models.
 *
 * @author Christian Dietz, University of Konstanz
 */
public class KNIMEInputPanel extends SwingInputPanel {

    /**
     * Get a list of widget models used by this input panel.
     *
     * @return the list of {@link SettingsModelWidgetModel}s.
     */
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
