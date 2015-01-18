package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.DataType;
import org.knime.core.data.def.DoubleCell;
import org.knime.knip.scijava.commands.adapter.OutputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Double to {@link DoubleCell}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = OutputAdapterPlugin.class)
public class DoubleOutputAdapter implements
		OutputAdapterPlugin<Double, DoubleCell> {

	@Override
	public DoubleCell createCell(Double o) {
		return new DoubleCell(o);
	}

	@Override
	public Class<Double> getSourceType() {
		return Double.class;
	}

	@Override
	public DataType getDataCellType() {
		return DoubleCell.TYPE;
	}

}
