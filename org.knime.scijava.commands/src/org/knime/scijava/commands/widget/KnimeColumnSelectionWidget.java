package org.knime.scijava.commands.widget;

import java.awt.event.ItemEvent;

import javax.swing.JPanel;

import org.knime.core.data.DataValue;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.util.ColumnSelectionComboxBox;
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

	private final JPanel box;
	private final String inputName;
	private String selected;

	@Parameter
	private InputAdapterService ias;
	@Parameter
	private InputDataRowService irs;
	@Parameter
	private LogService log;
	@Parameter
	private SimpleColumnMappingService colMapping;
	@Parameter
	private NodeDialogSettingsService settings;

	private final ColumnSelectionComboxBox colbox;

	public KnimeColumnSelectionWidget(final WidgetModel model,
			final Context context) {
		context.inject(this);

		set(model);
		inputName = model.getItem().getName();

		// find columns that can be converted into the target value
		final Class<? extends DataValue> inputClass = ias
				.getMatchingInputValueClass(model.getItem().getType());

		colbox = new ColumnSelectionComboxBox("", inputClass);
		final String mappedColumn = colMapping.getMappedColumn(inputName);
		try {
			colbox.update(irs.getInputDataTableSpec(), mappedColumn);
		} catch (final NotConfigurableException e) {
			log.warn(e); // TODO: Fail harder?
		}
		selected = colbox.getSelectedColumn();

		colbox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				selected = colbox.getSelectedColumn();
				colMapping.setMappedColumn(inputName, selected);
				settings.setValue(model.getItem(), selected);
			}
		});

		box = new JPanel();
		box.add(colbox);
	}

	@Override
	public String getValue() {
		return selected;
	}

	@Override
	public JPanel getComponent() {
		return box;
	}

	@Override
	public boolean supports(final WidgetModel model) {
		return true;
	};

	@Override
	public Class<JPanel> getComponentType() {
		return JPanel.class;
	}

	@Override
	protected void doRefresh() {
		try {
			colbox.update(irs.getInputDataTableSpec(), getValue());
		} catch (final NotConfigurableException e) {
			log.warn(e);
		}
		box.revalidate();
	}
}
