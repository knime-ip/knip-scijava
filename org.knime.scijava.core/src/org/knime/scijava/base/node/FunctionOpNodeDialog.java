package org.knime.scijava.base.node;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.scijava.Context;
import org.scijava.ops2.FunctionOp;
import org.scijava.struct2.Struct;
import org.scijava.struct2.StructInstance;
import org.scijava.swing.widget2.SwingWidgetPanelFactory;
import org.scijava.swing.widget2.SwingWidgetPanelFactory.WidgetPanel;
import org.scijava.widget2.WidgetService;

public class FunctionOpNodeDialog<I, O> extends NodeDialogPane {

	private Context m_ctx = new Context();
	private WidgetService m_widgets;

	NodeStructInstance<?> m_structInstance;

	public FunctionOpNodeDialog(Struct struct, StructInstance<FunctionOp<I, O>> func) {
		m_widgets = m_ctx.getService(WidgetService.class);
		NodeStructInstance<I> structInstance = new NodeStructInstance<>(struct);
		SwingWidgetPanelFactory factory = new SwingWidgetPanelFactory();
		m_ctx.inject(factory);
		WidgetPanel<I> panel = (WidgetPanel<I>) m_widgets.createPanel(structInstance, factory);
		m_structInstance = structInstance;
		getPanel().add(panel.getComponent());
		getPanel().repaint();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		m_structInstance.saveSettingsTo(settings);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {
		try {
			m_structInstance.loadSettingsFrom(settings);
		} catch (InvalidSettingsException e) {
			throw new NotConfigurableException(e.getMessage());
		}
	}
}
