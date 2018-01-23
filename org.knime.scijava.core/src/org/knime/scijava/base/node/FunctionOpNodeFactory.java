package org.knime.scijava.base.node;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.scijava.base.InStruct;
import org.knime.scijava.base.MyFunctionOp;
import org.scijava.param.ParameterStructs;
import org.scijava.param.ValidityException;
import org.scijava.struct.Struct;
import org.scijava.struct.StructInstance;

public class FunctionOpNodeFactory extends NodeFactory<FunctionOpNodeModel> {

	private MyFunctionOp m_function;
	
	private Struct m_inStruct;

	public FunctionOpNodeFactory() {
		// FIXME will be served from outside later.
		m_function = new MyFunctionOp();
		try {
			m_inStruct = ParameterStructs.structOf(InStruct.class);
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public FunctionOpNodeModel createNodeModel() {
		StructInstance<MyFunctionOp> struct = null;
		try {
			struct = ParameterStructs.create(m_function);
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		FunctionOpNodeDialog diag = null;
		try {
			diag = new FunctionOpNodeDialog(m_inStruct, m_function);
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return diag;
	}

}
