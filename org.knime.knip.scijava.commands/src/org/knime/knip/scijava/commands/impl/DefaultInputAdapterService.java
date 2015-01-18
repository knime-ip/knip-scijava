package org.knime.knip.scijava.commands.impl;

import org.knime.core.data.DataValue;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.knime.knip.scijava.commands.adapter.InputAdapterPlugin;
import org.knime.knip.scijava.commands.adapter.InputAdapterService;
import org.scijava.plugin.AbstractSingletonService;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of InputAdapterService.
 * 
 * Rather inefficient since {@link #getMatchingInputAdapter(Class, Class)}
 * searches linearly through all implementations of InputAdapterPlugin.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@SuppressWarnings("rawtypes")
@Plugin(type = InputAdapterService.class)
public class DefaultInputAdapterService extends
		AbstractSingletonService<InputAdapterPlugin> implements
		InputAdapterService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<InputAdapterPlugin> getPluginType() {
		return InputAdapterPlugin.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public InputAdapter getMatchingInputAdapter(
			Class<? extends DataValue> dataValueClass, Class<?> valueClass) {
		for (InputAdapterPlugin p : this.getInstances()) {
			if (p.getDataValueType().isAssignableFrom(dataValueClass)
					&& valueClass.isAssignableFrom(p.getType())) {
				return p;
			}
		}

		return null;
	}

}
