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
import org.scijava.struct2.Struct;
import org.scijava.swing.widget2.SwingWidgetPanelFactory;
import org.scijava.swing.widget2.SwingWidgetPanelFactory.WidgetPanel;
import org.scijava.widget2.WidgetService;

public class FunctionNodeDialog<I, O> extends NodeDialogPane {

	private final NodeDialogStructInstance<Function<I, O>> m_func;

	private final Context m_ctx = new Context();

	private final WidgetService m_widgets;

	private final NodeInputStructInstance<I> m_nodeInput;

	public FunctionNodeDialog(final Function<I, O> func)
			throws ValidityException, InstantiationException, IllegalAccessException {
		m_func = new NodeDialogStructInstance<>(ParameterStructs.structOf(func.getClass()), func);

		final Struct inStruct = ParameterStructs.structOf(m_func.member("input").member().getRawType());
		final Class<I> type = (Class<I>) m_func.member("input").member().getRawType();
		m_nodeInput = new NodeInputStructInstance<>(inStruct, type.newInstance());

		m_widgets = m_ctx.getService(WidgetService.class);
		final SwingWidgetPanelFactory factory = new SwingWidgetPanelFactory();
		WidgetPanel<Function<I, O>> panel = (WidgetPanel<Function<I, O>>) m_widgets.createPanel(m_func, factory);
		getPanel().add(panel.getComponent());
		panel = (WidgetPanel<Function<I, O>>) m_widgets.createPanel(m_nodeInput, factory);
		getPanel().add(panel.getComponent());
		getPanel().repaint();
	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
		m_func.saveSettingsTo(settings);
		m_nodeInput.saveSettingsTo(settings);
	}

	@Override
	protected void loadSettingsFrom(final NodeSettingsRO settings, final DataTableSpec[] specs)
			throws NotConfigurableException {
		try {
			m_func.loadSettingsFrom(settings);
			m_func.update(specs[0]);
			m_nodeInput.loadSettingsFrom(settings);
			m_nodeInput.update(specs[0]);
		} catch (final InvalidSettingsException e) {
			throw new NotConfigurableException(e.getMessage());
		}
	}
}
