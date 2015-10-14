package org.knime.knip.scijava.commands.adapter;

import java.util.Collection;

import org.knime.core.data.DataType;
import org.scijava.plugin.AbstractSingletonService;

/**
 * Abstract implementation of some convenience methods if
 * {@link InputAdapterService}.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
public abstract class AbstractInputAdapterService extends
		AbstractSingletonService<InputAdapter> implements InputAdapterService {

	@Override
	public Collection<InputAdapter> getMatchingInputAdapters(
			final DataType type) {
		// TODO: pray this works on all cases or what?
		return getMatchingInputAdapters(type.getValueClasses().get(0));
	}

}
