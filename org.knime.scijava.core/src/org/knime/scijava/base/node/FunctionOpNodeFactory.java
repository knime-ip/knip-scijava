package org.knime.scijava.base.node;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.scijava.base.MyFunctionOp;
import org.scijava.param.ParameterStructs;
import org.scijava.struct.StructInstance;

public class FunctionOpNodeFactory extends NodeFactory<FunctionOpNodeModel> {

	private MyFunctionOp m_function;

	public FunctionOpNodeFactory() {
		// FIXME will be served from outside later.
		m_function = new MyFunctionOp();
	}

	@Override
	public FunctionOpNodeModel createNodeModel() {
		StructInstance<MyFunctionOp> struct = ParameterStructs.create(m_function);
		return new FunctionOpNodeModel(struct);
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	public NodeView<FunctionOpNodeModel> createNodeView(int viewIndex, FunctionOpNodeModel nodeModel) {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new FunctionOpNodeDialog(m_function);
	}

}
