package org.knime.scijava.commands.widget;

import java.awt.event.ItemEvent;

import javax.swing.JPanel;

import org.knime.core.data.DataValue;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.util.ColumnSelectionPanel;
import org.knime.core.node.util.DataValueColumnFilter;
import org.knime.scijava.commands.adapter.InputAdapterService;
import org.knime.scijava.commands.io.InputDataRowService;
import org.knime.scijava.commands.settings.NodeDialogSettingsService;
import org.knime.scijava.commands.simplemapping.SimpleColumnMappingService;
import org.scijava.Context;
import org.scijava.Priority;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.ui.swing.widget.SwingInputWidget;
import org.scijava.widget.WidgetModel;

public class KnimeColumnSelectionWidget extends SwingInputWidget<String> {

	public static final double PRIORITY = Priority.NORMAL_PRIORITY;

	private final String m_inputName;
	private String m_selected;
	private final ColumnSelectionPanel m_colbox;

	@Parameter
	private InputAdapterService m_ias;
	@Parameter
	private InputDataRowService m_irs;
	@Parameter
	private LogService m_log;
	@Parameter
	private SimpleColumnMappingService m_colMapping;
	@Parameter
	private NodeDialogSettingsService m_settings;

	public KnimeColumnSelectionWidget(final WidgetModel model,
			final Context context) {
		context.inject(this);

		set(model);
		m_inputName = model.getItem().getName();

		// find columns that can be converted into the target value
		final Class<? extends DataValue> inputClass = m_ias
				.getMatchingInputValueClass(model.getItem().getType());

		// optional columns get the option to select <none>
		boolean isOptional = !model.getItem().isRequired();

		m_colbox = new ColumnSelectionPanel(null,
				new DataValueColumnFilter(inputClass), isOptional);
		final String mappedColumn = m_colMapping.getMappedColumn(m_inputName);
		try {
			m_colbox.update(m_irs.getInputDataTableSpec(), mappedColumn);
		} catch (final NotConfigurableException e) {
			m_log.warn(e); // TODO: Fail harder?
		}
		m_colbox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				updateState(model);
			}
		});

		// set initial selection
		updateState(model);
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
