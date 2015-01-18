package org.knime.knip.scijava.commands.adapter.basic;

import org.knime.core.data.DataType;
import org.knime.core.data.def.StringCell;
import org.knime.knip.scijava.commands.adapter.OutputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Character to {@link StringCell}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = OutputAdapterPlugin.class)
public class CharOutputAdapter implements
		OutputAdapterPlugin<Character, StringCell> {

	@Override
	public StringCell createCell(Character o) {
		return new StringCell(o.toString());
	}

	@Override
	public Class<Character> getSourceType() {
		return Character.class;
	}

	@Override
	public DataType getDataCellType() {
		return StringCell.TYPE;
	}

}
