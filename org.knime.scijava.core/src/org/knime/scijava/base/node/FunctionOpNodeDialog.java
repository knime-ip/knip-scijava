package org.knime.scijava.base.node;

import java.util.function.Function;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.scijava.Context;
import org.scijava.param2.ParameterStructs;
import org.scijava.param2.ValidityException;
import org.scijava.swing.widget2.SwingWidgetPanelFactory;
import org.scijava.swing.widget2.SwingWidgetPanelFactory.WidgetPanel;
import org.scijava.widget2.WidgetService;

public class FunctionOpNodeDialog<I, O> extends NodeDialogPane {

	private final NodeStructInstance<Function<I, O>> m_func;

	private final Context m_ctx = new Context();

	private final WidgetService m_widgets;

	public FunctionOpNodeDialog(final Function<I, O> func) throws ValidityException {
		m_func = new NodeStructInstance<>(ParameterStructs.structOf(func.getClass()), func);
		m_widgets = m_ctx.getService(WidgetService.class);
		final SwingWidgetPanelFactory factory = new SwingWidgetPanelFactory();
		m_ctx.inject(factory);
		final WidgetPanel<Function<I, O>> panel = (WidgetPanel<Function<I, O>>) m_widgets.createPanel(m_func, factory);
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
		} catch (final InvalidSettingsException e) {
			throw new NotConfigurableException(e.getMessage());
		}
	}
}
