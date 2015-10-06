package org.knime.knip.scijava.commands;

import org.knime.knip.scijava.commands.adapter.OutputAdapter;
import org.knime.knip.scijava.commands.adapter.OutputAdapterService;
import org.scijava.convert.ConvertService;
import org.scijava.plugin.AbstractSingletonService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of OutputAdapterService.
 * 
 * Rather inefficient since {@link #getMatchingOutputAdapter(Class)} searches
 * linearly through all existing implementations of OutputAdapterPlugin.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@SuppressWarnings("rawtypes")
@Plugin(type = OutputAdapterService.class)
public class DefaultOutputAdapterService extends
		AbstractSingletonService<OutputAdapter> implements OutputAdapterService {

	@Parameter
	private ConvertService cs;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<OutputAdapter> getPluginType() {
		return OutputAdapter.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public OutputAdapter getMatchingOutputAdapter(final Class<?> valueClass) {

		//TODO we can potentially cache the detected converters in a HashMap here here...
		for (final OutputAdapter outputAdapter : this.getInstances()) {
			if (outputAdapter.getInputType().isAssignableFrom(valueClass)) {
				return outputAdapter;
			}
		}
		
		return null;
	}

}
