package org.knime.scijava.commands.adapter.basic;

import org.knime.core.data.MissingCell;
import org.knime.core.data.MissingValue;
import org.knime.scijava.commands.adapter.AbstractOutputAdapter;
import org.knime.scijava.commands.adapter.OutputAdapter;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Fallback adapter for "result" output parameters.
 *
 * @author gabriel
 *
 */

@Plugin(type = OutputAdapter.class, priority = Priority.VERY_LOW_PRIORITY)
public class ObjectOutputAdapter
		extends AbstractOutputAdapter<Object, MissingValue> {

	@Override
	public Class<MissingValue> getOutputType() {
		return MissingValue.class;
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
