package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.DataType;
import org.knime.core.data.def.LongCell;
import org.knime.knip.scijava.commands.adapter.OutputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Long to {@link LongCell}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = OutputAdapterPlugin.class)
public class LongOutputAdapter implements
		OutputAdapterPlugin<Long, LongCell> {

	@Override
	public LongCell createCell(Long o) {
		return new LongCell(o);
	}

	@Override
	public Class<Long> getSourceType() {
		return Long.class;
	}

	@Override
	public DataType getDataCellType() {
		return LongCell.TYPE;
	}

}
