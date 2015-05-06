package org.knime.knip.scijava.commands.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import org.knime.core.data.DataValue;
import org.knime.knip.scijava.commands.adapter.AbstractInputAdapterService;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.knime.knip.scijava.commands.adapter.InputAdapterPlugin;
import org.knime.knip.scijava.commands.adapter.InputAdapterService;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of InputAdapterService.
 * 
 * Rather inefficient since {@link #getMatchingInputAdapter(Class, Class)}
 * searches linearly through all implementations of InputAdapterPlugin.
 * 
 * TODO: As soon as Scijava AbstractSingletonService listens to addPlugin
 * events, we need to turn off m_processed flag accordingly.
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
@SuppressWarnings("rawtypes")
@Plugin(type = InputAdapterService.class)
public class DefaultInputAdapterService extends AbstractInputAdapterService {

	private WeakHashMap<Class<? extends DataValue>, Set<InputAdapterPlugin>> m_pluginsByDataValue = new WeakHashMap<Class<? extends DataValue>, Set<InputAdapterPlugin>>();

	/* whether plugin instances where sorted into m_pluginsByDataValue already */
	private boolean m_processed = false;

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
	@Override
	public <D extends DataValue, T> InputAdapter getMatchingInputAdapter(
			Class<D> dataValueClass, Class<T> valueClass) {
		if (!m_processed) {
			processInstances();
		}

		// go through all the plugins the the Set matching the dataValueClass
		Set<InputAdapterPlugin> plugins = m_pluginsByDataValue
				.get(dataValueClass);

		if (plugins == null) {
			// no adapters for dataValueClass
			return null;
		}

		for (InputAdapterPlugin p : plugins) {
			if (valueClass.isAssignableFrom(p.getType())) {
				// found a matching plugin
				return p;
			}
		}

		// none of the plugins matched
		return null;
	}

	@Override
	public Collection<InputAdapterPlugin> getMatchingInputAdapters(
			Class<? extends DataValue> dataValueClass) {
		if (!m_processed) {
			processInstances();
		}

		return m_pluginsByDataValue.get(dataValueClass);
	}

	/*
	 * Process the plugin instances (sort them into the m_pluginByDataValue map
	 * for faster access later on)
	 */
	private void processInstances() {
		for (InputAdapterPlugin p : this.getInstances()) {
			Class<? extends DataValue> type = p.getDataValueType();

			Set<InputAdapterPlugin> set = m_pluginsByDataValue.get(type);

			if (set == null) {
				set = new HashSet<InputAdapterPlugin>(1);
				m_pluginsByDataValue.put(type, set);
			}

			set.add(p);
		}

		m_processed = true;
	}
}
