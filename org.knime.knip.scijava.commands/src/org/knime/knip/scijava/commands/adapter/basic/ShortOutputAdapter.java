package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.def.IntCell;
import org.knime.knip.scijava.commands.AbstractOutputAdapter;
import org.knime.knip.scijava.commands.adapter.OutputAdapter;
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
	public IntCell createCell(Short o) {
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
