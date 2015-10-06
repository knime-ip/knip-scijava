package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.def.DoubleCell;
import org.knime.knip.scijava.commands.AbstractOutputAdapter;
import org.knime.knip.scijava.commands.adapter.OutputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Float to {@link DoubleCell}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = OutputAdapter.class)
public class FloatOutputAdapter
		extends AbstractOutputAdapter<Float, DoubleCell> {

	@Override
	public DoubleCell createCell(Float o) {
		return new DoubleCell(o);
	}

	@Override
	public Class<DoubleCell> getOutputType() {
		return DoubleCell.class;
	}

	@Override
	public Class<Float> getInputType() {
		return Float.class;
	}

}
