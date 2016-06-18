package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.def.IntCell;
import org.knime.scijava.commands.adapter.AbstractOutputAdapter;
import org.knime.scijava.commands.adapter.OutputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Short to {@link IntCell}.
 *
 * @author Jonathan Hale (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 *
 */
@Plugin(type = OutputAdapter.class)
public class ShortOutputAdapter extends AbstractOutputAdapter<Short, IntCell> {

	@Override
	public IntCell createCell(final Short o) {
		return new IntCell(o);
	}

	@Override
	public Class<IntCell> getOutputType() {
		return IntCell.class;
	}

	@Override
	public Class<Short> getInputType() {
		return Short.class;
	}

}
