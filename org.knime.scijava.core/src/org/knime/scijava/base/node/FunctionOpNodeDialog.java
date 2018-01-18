package org.knime.scijava.base.node;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.scijava.Context;
import org.scijava.ValidityException;
import org.scijava.nwidget.NWidgetService;
import org.scijava.nwidget.swing.NSwingWidget;
import org.scijava.nwidget.swing.NSwingWidgetPanelFactory;
import org.scijava.struct.Struct;
import org.scijava.struct.StructInstance;

public class FunctionOpNodeDialog<I, O> extends NodeDialogPane {

	private Context m_ctx = new Context(NWidgetService.class);
	private NWidgetService m_widgets;

	public FunctionOpNodeDialog(Struct struct) throws ValidityException {
		m_widgets = m_ctx.getService(NWidgetService.class);
		StructInstance<I> structInstance = new NodeDialogStructInstance<I>(struct);
		NSwingWidgetPanelFactory factory = new NSwingWidgetPanelFactory();
		NSwingWidget panel = (NSwingWidget) m_widgets.createPanel(structInstance, factory);
		getPanel().add(panel.getComponent());
		getPanel().repaint();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
	}
	
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {
		super.loadSettingsFrom(settings, specs);
	}

}
