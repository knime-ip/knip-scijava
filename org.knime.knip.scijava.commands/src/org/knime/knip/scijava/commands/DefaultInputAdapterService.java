package org.knime.knip.scijava.commands;

import org.knime.core.data.DataValue;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.scijava.plugin.AbstractSingletonService;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of InputAdapterService.
 * 
 * Rather inefficient since {@link #getMatchingInputAdapter(Class, Class)}
 * searches linearly through all implementations of InputAdapter.
 * 
 * TODO: As soon as Scijava AbstractSingletonService listens to add events, we
 * need to turn off m_processed flag accordingly.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @author Christian Dietz, University of Konstanz
 */
@SuppressWarnings("rawtypes")
@Plugin(type = InputAdapterService.class)
public class DefaultInputAdapterService extends
		AbstractSingletonService<InputAdapter> implements InputAdapterService {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <D extends DataValue, T> InputAdapter getMatchingInputAdapter(
			final DataValue dataValue, final Class<T> valueClass) {

		// go through all the s the the Set matching the dataValueClass

		// TODO we can cache them all
		for (final InputAdapter<DataValue, Object> p : this.getInstances()) {
			if (p.canConvert(dataValue, valueClass)) {
				// found a matching
				return p;
			}
		}

		// none of the s matched
		return null;
	}

	@Override
	public Class<InputAdapter> getPluginType() {
		return InputAdapter.class;
	}
}
