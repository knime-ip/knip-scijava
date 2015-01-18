package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.DataType;
import org.knime.core.data.def.IntCell;
import org.knime.knip.scijava.commands.adapter.OutputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Byte to {@link IntCell}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = OutputAdapterPlugin.class)
public class ByteOutputAdapter implements
		OutputAdapterPlugin<Byte, IntCell> {

	@Override
	public IntCell createCell(Byte o) {
		return new IntCell(o);
	}

	@Override
	public Class<Byte> getSourceType() {
		return Byte.class;
	}

	@Override
	public DataType getDataCellType() {
		return IntCell.TYPE;
	}

}
