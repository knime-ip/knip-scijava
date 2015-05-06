package org.knime.knip.scijava.commands.adapter;

import java.util.Collection;

import org.knime.core.data.DataType;
import org.knime.core.data.DataValue;
import org.scijava.plugin.AbstractSingletonService;

/**
 * Abstract implementation of some convenience methods if
 * {@link InputAdapterService}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 *
 */
public abstract class AbstractInputAdapterService extends
		AbstractSingletonService<InputAdapterPlugin> implements
		InputAdapterService {

	@Override
	public Collection<InputAdapterPlugin> getMatchingInputAdapters(DataType type) {
		// TODO: pray this works on all cases or what?
		return getMatchingInputAdapters(type.getValueClasses().get(0));
	}

}
