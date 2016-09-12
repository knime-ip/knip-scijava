package org.knime.scijava.commands.widget;

import java.awt.event.ItemEvent;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.knime.core.data.DataCell;
import org.knime.core.data.convert.datacell.JavaToDataCellConverterFactory;
import org.knime.scijava.commands.converter.KNIMEConverterService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.swing.widget.SwingInputWidget;
import org.scijava.widget.InputWidget;
import org.scijava.widget.WidgetModel;

/**
 * Widget to select a DataType into which or from which to convert into the
 * module item for which this widget is created.
 *
 * @author Christian Dietz, University of Konstanz
 */
@Plugin(type = InputWidget.class)
public class DataTypeSelectionWidget extends SwingInputWidget<String> {

    @Parameter
    private KNIMEConverterService cs;

    // Internal parameters

    private String selectedItem;

    private JPanel panel;

    private JComboBox<String> comboBox;

    private final HashMap<String, String> nameToClass;

    private final HashMap<String, String> classToName;

    public DataTypeSelectionWidget() {
        nameToClass = new HashMap<>();
        classToName = new HashMap<>();
    }

    @Override
    public void set(final WidgetModel model) {
        super.set(model);

        /* find converter factories for the module item */
        final Collection<JavaToDataCellConverterFactory<?>> sourceFacs = cs
                .getMatchingFactories(model.getItem().getType());

        if (sourceFacs.size() == 0) {
            throw new IllegalArgumentException("No valid output available.");
        }

        for (final JavaToDataCellConverterFactory<?> fac : sourceFacs) {
            final Class<? extends DataCell> cellClass = fac.getDestinationType()
                    .getCellClass();
            nameToClass.put(cellClass.getSimpleName(), cellClass.getName());
            classToName.put(cellClass.getName(), cellClass.getSimpleName());
        }

        /* create a ComboBox for the converter factories */
        panel = new JPanel();
        comboBox = new JComboBox<>(
                nameToClass.keySet().toArray(new String[nameToClass.size()]));

        // set listener which updates the widget model after an item is selected
        comboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectedItem = (String) comboBox.getSelectedItem();
                updateModel();
            }
        });

        // set initial selection
        selectedItem = classToName.get(model.getValue());
        comboBox.setSelectedItem(selectedItem);
        panel.add(comboBox);
    }

    @Override
    public String getValue() {
        return nameToClass.get(selectedItem);
    }

    @Override
    public JPanel getComponent() {
        return panel;
    }

    @Override
    public boolean supports(final WidgetModel model) {
        return model instanceof SettingsModelWidgetModel
                && model.getItem().isOutput();
    }

    @Override
    public Class<JPanel> getComponentType() {
        return JPanel.class;
    }

    @Override
    protected void doRefresh() {
        comboBox.setSelectedItem(classToName.get(get().getValue()));
        panel.revalidate();
        panel.repaint();
    }
}
