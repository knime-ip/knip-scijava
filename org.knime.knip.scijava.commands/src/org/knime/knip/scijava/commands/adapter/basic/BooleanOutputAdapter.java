package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.DataType;
import org.knime.core.data.def.BooleanCell;
import org.knime.knip.scijava.commands.adapter.AbstractOutputAdapterPlugin;
import org.knime.knip.scijava.commands.adapter.OutputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Boolean to {@link BooleanCell}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = OutputAdapterPlugin.class)
public class BooleanOutputAdapter extends
		AbstractOutputAdapterPlugin<Boolean, BooleanCell> {

	@Override
	public BooleanCell createCell(Boolean o) {
		return (o) ? BooleanCell.TRUE : BooleanCell.FALSE;
	}

	@Override
	public Class<Boolean> getSourceType() {
		return Boolean.class;
	}

	@Override
	public DataType getDataCellType() {
		return BooleanCell.TYPE;
	}

	@Override
	public Class<BooleanCell> getOutputType() {
		return BooleanCell.class;
	}

	@Override
	public Class<Boolean> getInputType() {
		return Boolean.class;
	}

}
