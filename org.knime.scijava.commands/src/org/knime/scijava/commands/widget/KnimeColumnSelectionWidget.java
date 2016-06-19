package org.knime.scijava.commands.widget;

import java.awt.event.ItemEvent;
import java.util.Optional;

import javax.swing.JPanel;

import org.knime.core.data.DataValue;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.util.ColumnSelectionPanel;
import org.knime.core.node.util.DataValueColumnFilter;
import org.knime.scijava.commands.converter.ConverterCacheService;
import org.knime.scijava.commands.io.InputDataRowService;
import org.knime.scijava.commands.settings.NodeDialogSettingsService;
import org.knime.scijava.commands.simplemapping.SimpleColumnMappingService;
import org.scijava.Context;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.ui.swing.widget.SwingInputWidget;
import org.scijava.widget.WidgetModel;

public class KnimeColumnSelectionWidget extends SwingInputWidget<String> {

    private final String m_inputName;
    private String m_selected;
    private final ColumnSelectionPanel m_colbox;

    @Parameter
    private ConverterCacheService m_converterCache;
    @Parameter
    private InputDataRowService m_irs;
    @Parameter
    private LogService m_log;
    @Parameter
    private SimpleColumnMappingService m_colMapping;
    @Parameter
    private NodeDialogSettingsService m_settings;

    public KnimeColumnSelectionWidget(final WidgetModel model,
            final Context context, final String defaultColumn) {
        context.inject(this);

        set(model);
        m_inputName = model.getItem().getName();

        // find columns that can be converted into the target value
        final Optional<Class<DataValue>> inputClass2 = m_converterCache
                .getMatchingInputValueClass(model.getItem().getType());

        if (!inputClass2.isPresent()) {
            throw new IllegalArgumentException("No valid input available.");
        }

        // optional columns get the option to select <none>
        final boolean isOptional = !model.getItem().isRequired();

        m_colbox = new ColumnSelectionPanel(null,
                new DataValueColumnFilter(inputClass2.get()), isOptional);

        // set listener
        m_colbox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateState(model);
            }
        });

        // set initial selection
        if (defaultColumn == null || "".equals(defaultColumn)) {
            try {
                m_colbox.update(m_irs.getInputDataTableSpec(), null);
            } catch (final NotConfigurableException e) {
                m_log.warn(e);
            }
            updateState(model); // no default value set to first applicable
        } else {
            try {
                m_colbox.update(m_irs.getInputDataTableSpec(), defaultColumn);
                m_selected = defaultColumn;
                m_colMapping.setMappedColumn(m_inputName, m_selected);
                m_settings.setValue(model.getItem(), m_selected);
            } catch (final NotConfigurableException e) {
                m_log.warn("Could not select the column" + defaultColumn + e);
                // TODO: Fail harder?
                updateState(model);
            }
        }
    }

    private void updateState(final WidgetModel model) {
        m_selected = m_colbox.getSelectedColumn();
        m_colMapping.setMappedColumn(m_inputName, m_selected);
        m_settings.setValue(model.getItem(), m_selected);
    }

    @Override
    public String getValue() {
        return m_selected;
    }

    @Override
    public JPanel getComponent() {
        return m_colbox;
    }

    @Override
    public boolean supports(final WidgetModel model) {
        return true;
    }

    @Override
    public Class<JPanel> getComponentType() {
        return JPanel.class;
    }

    @Override
    protected void doRefresh() {
        try {
            m_colbox.update(m_irs.getInputDataTableSpec(), getValue());
        } catch (final NotConfigurableException e) {
            m_log.warn(e);
        }
        m_colbox.revalidate();
        m_colbox.repaint();
    }
}
