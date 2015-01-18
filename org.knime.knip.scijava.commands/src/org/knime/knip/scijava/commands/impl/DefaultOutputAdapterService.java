package org.knime.knip.scijava.commands.impl;

import org.knime.knip.scijava.commands.adapter.OutputAdapter;
import org.knime.knip.scijava.commands.adapter.OutputAdapterPlugin;
import org.knime.knip.scijava.commands.adapter.OutputAdapterService;
import org.scijava.plugin.AbstractSingletonService;
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
		AbstractSingletonService<OutputAdapterPlugin> implements
		OutputAdapterService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<OutputAdapterPlugin> getPluginType() {
		return OutputAdapterPlugin.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public OutputAdapter getMatchingOutputAdapter(Class<?> valueClass) {
		for (OutputAdapter outputAdapter : this.getInstances()) {
			if (outputAdapter.getSourceType().isAssignableFrom(valueClass)) {
				return outputAdapter;
			}
		}
		return null;
	}

}
