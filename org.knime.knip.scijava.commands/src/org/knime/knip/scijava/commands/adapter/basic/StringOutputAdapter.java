package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.DataType;
import org.knime.core.data.def.StringCell;
import org.knime.knip.scijava.commands.adapter.AbstractOutputAdapterPlugin;
import org.knime.knip.scijava.commands.adapter.OutputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for String to {@link StringCell}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = OutputAdapterPlugin.class)
public class StringOutputAdapter extends
		AbstractOutputAdapterPlugin<String, StringCell> {

	@Override
	public StringCell createCell(String o) {
		return new StringCell(o);
	}

	@Override
	public Class<String> getSourceType() {
		return String.class;
	}

	@Override
	public DataType getDataCellType() {
		return StringCell.TYPE;
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
