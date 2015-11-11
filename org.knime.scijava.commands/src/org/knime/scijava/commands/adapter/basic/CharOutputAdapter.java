package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.def.StringCell;
import org.knime.scijava.commands.adapter.AbstractOutputAdapter;
import org.knime.scijava.commands.adapter.OutputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Character to {@link StringCell}.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = OutputAdapter.class)
public class CharOutputAdapter
		extends AbstractOutputAdapter<Character, StringCell> {

	@Override
	public StringCell createCell(final Character o) {
		return new StringCell(o.toString());
	}

	@Override
	public Class<StringCell> getOutputType() {
		return StringCell.class;
	}

	@Override
	public Class<Character> getInputType() {
		return Character.class;
	}

}
