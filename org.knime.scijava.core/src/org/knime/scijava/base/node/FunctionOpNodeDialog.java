package org.knime.scijava.base.node;

import java.util.function.Function;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.scijava.param.ValidityException;
import org.scijava.struct.Struct;

public class FunctionOpNodeDialog<I, O> extends NodeDialogPane {

//	private Context m_ctx = new Context(NWidgetService.class);
//	private NWidgetService m_widgets;

	public FunctionOpNodeDialog(Struct struct, Function<I, O> func) throws ValidityException {
//		m_widgets = m_ctx.getService(NWidgetService.class);
//		StructInstance<I> structInstance = new NodeDialogStructInstance<I>(struct);
//		NSwingWidgetPanelFactory factory = new NSwingWidgetPanelFactory();
//		NSwingWidget panel = (NSwingWidget) m_widgets.createPanel(structInstance, factory);
//		getPanel().add(panel.getComponent());
//		getPanel().repaint();
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
	}
	
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, DataTableSpec[] specs) throws NotConfigurableException {
		super.loadSettingsFrom(settings, specs);
	}

}
