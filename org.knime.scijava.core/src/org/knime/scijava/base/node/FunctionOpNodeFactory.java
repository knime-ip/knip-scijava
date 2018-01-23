package org.knime.scijava.base.node;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.scijava.base.InStruct;
import org.knime.scijava.base.MyFunction;
import org.knime.scijava.base.OutStruct;
import org.scijava.param2.ParameterStructs;
import org.scijava.param2.ValidityException;
import org.scijava.struct2.Struct;
import org.scijava.struct2.StructInstance;

public class FunctionOpNodeFactory extends NodeFactory<FunctionOpNodeModel> {

	private StructInstance<MyFunction> m_functionStruct;

	private Struct m_inStruct;

	public FunctionOpNodeFactory() {
		// FIXME will be served from outside later.
		try {
			m_functionStruct = ParameterStructs.create(new MyFunction());
			Struct lala = ParameterStructs.structOf(MyFunction.class);
			m_inStruct = ParameterStructs.structOf(InStruct.class);
		} catch (ValidityException e) {
			throw new RuntimeException();
		}
	}

	@Override
	public FunctionOpNodeModel createNodeModel() {
		return new FunctionOpNodeModel(m_inStruct, m_functionStruct);
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
		return new FunctionOpNodeDialog(m_inStruct, m_functionStruct);
	}
}
