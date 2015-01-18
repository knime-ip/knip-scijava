package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.DataType;
import org.knime.core.data.def.IntCell;
import org.knime.knip.scijava.commands.adapter.OutputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Short to {@link IntCell}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = OutputAdapterPlugin.class)
public class ShortOutputAdapter implements OutputAdapterPlugin<Short, IntCell> {

	@Override
	public IntCell createCell(Short o) {
		return new IntCell(o);
	}

	@Override
	public Class<Short> getSourceType() {
		return Short.class;
	}

	@Override
	public DataType getDataCellType() {
		return IntCell.TYPE;
	}

}
