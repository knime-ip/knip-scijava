package org.knime.scijava.commands.widget;

import javax.swing.JPanel;

import org.knime.core.data.DataValue;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.SettingsModelColumnName;
import org.knime.core.node.util.ColumnSelectionComboxBox;
import org.knime.scijava.commands.adapter.OutputAdapter;
import org.knime.scijava.commands.adapter.OutputAdapterService;
import org.knime.scijava.commands.io.InputDataRowService;
import org.knime.scijava.commands.simplemapping.SimpleColumnMappingService;
import org.scijava.Context;
import org.scijava.Priority;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.ui.swing.widget.SwingInputWidget;
import org.scijava.widget.WidgetModel;

public class KNIMEColumSelectionWidget extends SwingInputWidget<String> {

	public static final double PRIORITY = Priority.NORMAL_PRIORITY;

	private final JPanel box;
	private final DefaultKNIMEWidgetModel kModel;
	private final SettingsModelColumnName settingsModel;
	private final String inputName;
	private String selected;

	@Parameter
	private OutputAdapterService oas;
	@Parameter
	private InputDataRowService irs;
	@Parameter
	private LogService log;
	@Parameter
	private SimpleColumnMappingService colMapping;

	public KNIMEColumSelectionWidget(final WidgetModel model,
			final Context context) {
		context.inject(this);

		set(model);
		kModel = (DefaultKNIMEWidgetModel) model;
		settingsModel = (SettingsModelColumnName) kModel.getSettingsModel();
		inputName = model.getItem().getName();

		// find columns that can be converted into the target value
		final Class<? extends DataValue> input = ((OutputAdapter<?, ?>) oas
				.getMatchingOutputAdapter(kModel.getItem().getType()))
						.getOutputType();

		@SuppressWarnings("unchecked")
		final ColumnSelectionComboxBox colbox = new ColumnSelectionComboxBox("",
				input);
		try {
			colbox.update(irs.getInputDataTableSpec(), null);
		} catch (final NotConfigurableException e) {
			log.warn(e);
		}

		colbox.addItemListener(e -> {
			selected = colbox.getSelectedColumn();
			colMapping.setMappedColumn(inputName, colbox.getSelectedColumn());
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
		box.revalidate(); // TODO is this a good idea?
	}
}
