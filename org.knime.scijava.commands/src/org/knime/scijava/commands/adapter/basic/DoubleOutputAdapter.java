package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.def.DoubleCell;
import org.knime.scijava.commands.AbstractOutputAdapter;
import org.knime.scijava.commands.adapter.OutputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Double to {@link DoubleCell}.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = OutputAdapter.class)
public class DoubleOutputAdapter
		extends AbstractOutputAdapter<Double, DoubleCell> {

	@Override
	public DoubleCell createCell(final Double o) {
		return new DoubleCell(o);
	}

	@Override
	public Class<DoubleCell> getOutputType() {
		return DoubleCell.class;
	}

	@Override
	public Class<Double> getInputType() {
		return Double.class;
	}

}
