package org.knime.scijava.base.node;

import java.util.function.Function;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.scijava.base.MultiplicationFunction;
import org.scijava.param2.ValidityException;

public class FunctionOpNodeFactory extends NodeFactory<FunctionNodeModel<?, ?>> {

	private final Function<?, ?> m_function = new MultiplicationFunction();

	public FunctionOpNodeFactory() {
		// function will be served from the outside, later
	}

	@Override
	public FunctionNodeModel<?, ?> createNodeModel() {
		try {
			return new FunctionNodeModel<>(m_function);
		} catch (final ValidityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	public NodeView<FunctionNodeModel<?, ?>> createNodeView(final int viewIndex,
			final FunctionNodeModel<?, ?> nodeModel) {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		try {
			return new FunctionOpNodeDialog<>(m_function);
		} catch (final ValidityException e) {
			throw new RuntimeException(e);
		}
	}
}
