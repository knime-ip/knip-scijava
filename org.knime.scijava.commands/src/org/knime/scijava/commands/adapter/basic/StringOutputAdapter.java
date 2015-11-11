package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.def.StringCell;
import org.knime.scijava.commands.adapter.AbstractOutputAdapter;
import org.knime.scijava.commands.adapter.OutputAdapter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for String to {@link StringCell}.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = OutputAdapter.class)
public class StringOutputAdapter
		extends AbstractOutputAdapter<String, StringCell> {

	@Override
	public StringCell createCell(final String o) {
		return new StringCell(o);
	}

	@Override
	public Class<StringCell> getOutputType() {
		return StringCell.class;
	}

	@Override
	public Class<String> getInputType() {
		return String.class;
	}

}
