package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.MissingCell;
import org.knime.scijava.commands.adapter.AbstractOutputAdapter;
import org.knime.scijava.commands.adapter.OutputAdapter;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Fall-back adapter for module outputs of type "Object", will create a missing
 * value
 *
 * @author gabriel
 *
 */

@Plugin(type = OutputAdapter.class, priority = Priority.VERY_LOW_PRIORITY)
public class MissingCellOutputAdapter
		extends AbstractOutputAdapter<Object, MissingCell> {

	@Override
	public Class<MissingCell> getOutputType() {
		return MissingCell.class;
	}

	@Override
	public Class<Object> getInputType() {
		return Object.class;
	}

	@Override
	protected MissingCell createCell(Object obj) {
		return new MissingCell("Unable to create outputcell");
	}

}
