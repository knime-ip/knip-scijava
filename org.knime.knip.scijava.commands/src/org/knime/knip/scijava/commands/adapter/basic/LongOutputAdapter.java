package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.def.LongCell;
import org.knime.knip.scijava.commands.AbstractOutputAdapter;
import org.knime.knip.scijava.commands.adapter.OutputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Long to {@link LongCell}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = OutputAdapter.class)
public class LongOutputAdapter extends AbstractOutputAdapter<Long, LongCell> {

	@Override
	public LongCell createCell(Long o) {
		return new LongCell(o);
	}

	@Override
	public Class<LongCell> getOutputType() {
		return LongCell.class;
	}

	@Override
	public Class<Long> getInputType() {
		return Long.class;
	}

}
