package org.knime.scijava.commands.widget;

import java.awt.event.ItemEvent;
import java.util.Optional;

import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataValue;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.util.ColumnSelectionPanel;
import org.knime.core.node.util.DataValueColumnFilter;
import org.knime.scijava.commands.converter.KNIMEConverterService;
import org.knime.scijava.commands.settings.models.SettingsModelColumnSelection;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.swing.widget.SwingInputWidget;
import org.scijava.widget.InputWidget;
import org.scijava.widget.WidgetModel;

/**
 * Widget for selecting a column to fill a certain module item.
 *
 * @author Christian Dietz, University of Konstanz
 */
@Plugin(type = InputWidget.class)
public class ColumnSelectionWidget extends SwingInputWidget<String> {

    @Parameter
    private KNIMEConverterService convCache;

    // Internal parameters

    private String selected;

    private ColumnSelectionPanel colBox;

    @SuppressWarnings("unchecked")
    @Override
    public void set(final WidgetModel model) {
        super.set(model);

        // find columns that can be converted into the target value
        final Optional<Class<DataValue>> inputClass = convCache
                .getMatchingInputValueClass(model.getItem().getType());

        if (!inputClass.isPresent()) {
            throw new IllegalArgumentException("No valid input available.");
        }

        // optional columns get the option to select <none>
        final boolean isOptional = !model.getItem().isRequired();

        colBox = new ColumnSelectionPanel(null,
                new DataValueColumnFilter(inputClass.get()), isOptional);

        // set listener
        colBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selected = colBox.getSelectedColumn();
                updateModel();
            }
        });

        // set initial selection

        final DataTableSpec spec = getSpecFromModel();

        if (spec != null) {
            try {
                colBox.update(getSpecFromModel(), null);
            } catch (final NotConfigurableException e) {
                throw new IllegalStateException(e);
            }
        }
        selected = (String) model.getValue();
    }

    @Override
    public String getValue() {
        return selected;
    }

    @Override
    public JPanel getComponent() {
        return colBox;
    }

    @Override
    public boolean supports(final WidgetModel model) {
        return model instanceof SettingsModelWidgetModel
                && (((SettingsModelWidgetModel) model)
                        .getSettingsModel() instanceof SettingsModelColumnSelection);
    }

    @Override
    public Class<JPanel> getComponentType() {
        return JPanel.class;
    }

    @Override
    protected void doRefresh() {
        try {
            final DataTableSpec spec = getSpecFromModel();
            if (spec != null) {
                colBox.update(getSpecFromModel(), (String) get().getValue());
                colBox.revalidate();
                colBox.repaint();
            }
        } catch (final NotConfigurableException e) {
            throw new IllegalStateException(e);
        }
    }

    private DataTableSpec getSpecFromModel() {
        return ((SettingsModelColumnSelection) ((SettingsModelWidgetModel) get())
                .getSettingsModel()).getSpec();
    }
}
