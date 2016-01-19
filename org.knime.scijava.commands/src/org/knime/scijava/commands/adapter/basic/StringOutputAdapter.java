package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.StringValue;
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
		extends AbstractOutputAdapter<String, StringValue> {

	@Override
	public StringCell createCell(final String o) {
		return new StringCell(o);
	}

	@Override
	public Class<StringValue> getOutputType() {
		return StringValue.class;
	}

	@Override
	public Class<String> getInputType() {
		return String.class;
	}

}
