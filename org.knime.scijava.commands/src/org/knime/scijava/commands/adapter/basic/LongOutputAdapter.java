package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.LongValue;
import org.knime.core.data.def.LongCell;
import org.knime.scijava.commands.adapter.AbstractOutputAdapter;
import org.knime.scijava.commands.adapter.OutputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Long to {@link LongCell}.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = OutputAdapter.class)
public class LongOutputAdapter extends AbstractOutputAdapter<Long, LongValue> {

	@Override
	public LongCell createCell(final Long o) {
		return new LongCell(o);
	}

	@Override
	public Class<LongValue> getOutputType() {
		return LongValue.class;
	}

	@Override
	public Class<Long> getInputType() {
		return Long.class;
	}

}
