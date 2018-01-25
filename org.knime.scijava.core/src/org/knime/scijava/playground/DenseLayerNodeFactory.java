package org.knime.scijava.playground;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.scijava.param2.ValidityException;

public class DenseLayerNodeFactory extends NodeFactory<KerasModelToKerasModelFunctionNodeModel> {

	private final KerasModelToKerasModelFunction m_function = new KerasDenseLayerFunction();

	public DenseLayerNodeFactory() {
		// function will be served from the outside, later
	}

	@Override
	public KerasModelToKerasModelFunctionNodeModel createNodeModel() {
		try {
			return new KerasModelToKerasModelFunctionNodeModel(m_function);
		} catch (final ValidityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected int getNrNodeViews() {
		return 0;
	}

	@Override
	public NodeView<KerasModelToKerasModelFunctionNodeModel> createNodeView(final int viewIndex,
			final KerasModelToKerasModelFunctionNodeModel nodeModel) {
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		try {
			return new KerasModelToKerasModelFunctionNodeDialog(m_function);
		} catch (final ValidityException e) {
			throw new RuntimeException(e);
		}
	}
}
