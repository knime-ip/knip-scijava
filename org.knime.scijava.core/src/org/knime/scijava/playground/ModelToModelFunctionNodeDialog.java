package org.knime.scijava.playground;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.scijava.base.node.NodeDialogStructInstance;
import org.scijava.Context;
import org.scijava.param2.ParameterStructs;
import org.scijava.param2.ValidityException;
import org.scijava.swing.widget2.SwingWidgetPanelFactory;
import org.scijava.swing.widget2.SwingWidgetPanelFactory.WidgetPanel;
import org.scijava.widget2.WidgetService;

public class ModelToModelFunctionNodeDialog extends NodeDialogPane {

	private final NodeDialogStructInstance<ModelToModelFunction<?, ?>> m_func;

	private final Context m_ctx = new Context();

	private final WidgetService m_widgets;

	public ModelToModelFunctionNodeDialog(final KerasModelToKerasModelFunction func) throws ValidityException {
		m_func = new NodeDialogStructInstance<>(ParameterStructs.structOf(func.getClass()), func);
		m_widgets = m_ctx.getService(WidgetService.class);
		final SwingWidgetPanelFactory factory = new SwingWidgetPanelFactory();
		final WidgetPanel<ModelToModelFunction<?, ?>> panel = (WidgetPanel<ModelToModelFunction<?, ?>>) m_widgets
				.createPanel(m_func, factory);
		getPanel().add(panel.getComponent());
		getPanel().repaint();
	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
		m_func.saveSettingsTo(settings);
	}

	@Override
	protected void loadSettingsFrom(final NodeSettingsRO settings, final DataTableSpec[] specs)
			throws NotConfigurableException {
		try {
			m_func.loadSettingsFrom(settings);
			m_func.update(specs[0]);
		} catch (final InvalidSettingsException e) {
			throw new NotConfigurableException(e.getMessage());
		}
	}
}
