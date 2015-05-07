package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.knip.scijava.commands.adapter.AbstractOutputAdapterPlugin;
import org.knime.knip.scijava.commands.adapter.OutputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Float to {@link DoubleCell}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = OutputAdapterPlugin.class)
public class FloatOutputAdapter extends
		AbstractOutputAdapterPlugin<Float, DoubleCell> {

	@Override
	public DoubleCell createCell(Float o) {
		return new DoubleCell(o);
	}

	@Override
	public Class<Float> getSourceType() {
		return Float.class;
	}

	@Override
	public DataType getDataCellType() {
		return DoubleCell.TYPE;
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
