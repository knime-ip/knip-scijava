package org.knime.scijava.commands.widget;

import javax.swing.JPanel;

import org.knime.core.data.DataValue;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.scijava.commands.adapter.OutputAdapter;
import org.knime.scijava.commands.adapter.OutputAdapterService;
import org.scijava.Context;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.ui.swing.widget.SwingInputWidget;
import org.scijava.widget.WidgetModel;

public class KNIMEColumSelectionWidget extends SwingInputWidget<String> {

	public static final double PRIORITY = Priority.NORMAL_PRIORITY;

	private JPanel box;
	private DialogComponentColumnNameSelection selectionComponent;
	private DefaultKNIMEWidgetModel kModel;
	private SettingsModelString settingsModel;

	@Parameter
	private OutputAdapterService oas;

	public KNIMEColumSelectionWidget(WidgetModel model, Context context) {
		context.inject(this);

		set(model);
		kModel = (DefaultKNIMEWidgetModel) model;

		Class<? extends DataValue> input = ((OutputAdapter<?, ?>) oas
				.getMatchingOutputAdapter(kModel.getItem().getType()))
						.getOutputType();

		settingsModel = (SettingsModelString) kModel.getSettingsModel();
		selectionComponent = new DialogComponentColumnNameSelection(settingsModel,
				model.getWidgetLabel(), 0, true, DataValue.class);


		box = selectionComponent.getComponentPanel();
	}

	@Override
	public String getValue() {
		return settingsModel.getStringValue();
	}

	@Override
	public JPanel getComponent() {
		return box;
	}

	@Override
	public boolean supports(WidgetModel model) {
		return true;
	};

	@Override
	public Class<JPanel> getComponentType() {
		return JPanel.class;
	}

	@Override
	protected void doRefresh() {
	}
}
