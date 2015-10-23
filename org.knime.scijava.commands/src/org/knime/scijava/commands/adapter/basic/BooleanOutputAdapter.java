package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.def.BooleanCell;
import org.knime.scijava.commands.AbstractOutputAdapter;
import org.knime.scijava.commands.adapter.OutputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Boolean to {@link BooleanCell}.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = OutputAdapter.class)
public class BooleanOutputAdapter
		extends AbstractOutputAdapter<Boolean, BooleanCell> {

	@Override
	public BooleanCell createCell(final Boolean o) {
		return (o) ? BooleanCell.TRUE : BooleanCell.FALSE;
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