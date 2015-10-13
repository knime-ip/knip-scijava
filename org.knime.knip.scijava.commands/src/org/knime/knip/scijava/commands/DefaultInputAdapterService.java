package org.knime.knip.scijava.commands;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;

import org.knime.core.data.DataValue;
import org.knime.knip.scijava.commands.adapter.AbstractInputAdapterService;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.knime.knip.scijava.commands.adapter.InputAdapterService;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of InputAdapterService. Rather inefficient since
 * {@link #getMatchingInputAdapter(Class, Class)} searches linearly through all
 * implementations of InputAdapterPlugin.
 * 
 * TODO: As soon as Scijava AbstractSingletonService listens to addPlugin
 * events, we need to turn off m_processed flag accordingly.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @author Christian Dietz, University of Konstanz
 */
@SuppressWarnings("rawtypes")
@Plugin(type = InputAdapterService.class)
public class DefaultInputAdapterService extends AbstractInputAdapterService {

	private WeakHashMap<Class<? extends DataValue>, Set<InputAdapter>> m_pluginsByDataValue = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<InputAdapter> getPluginType() {
		return InputAdapter.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <D extends DataValue, T> InputAdapter getMatchingInputAdapter(
			Class<D> dataValueClass, Class<T> valueClass) {
		if (m_pluginsByDataValue == null) {
			processInstances();
		}

		// go through all the plugins the the Set matching the dataValueClass
		Collection<InputAdapter> plugins = getMatchingInputAdapters(dataValueClass);

		if (plugins == null) {
			// no adapters for dataValueClass
			return null;
		}

		for (InputAdapter p : plugins) {
			if (valueClass.isAssignableFrom(p.getOutputType())) {
				// found a matching plugin
				return p;
			}
		}

		// none of the plugins matched
		return null;
	}

	/**
	 * Messy (TODO) method which recursively searches for all super classes which
	 * extends DataValue.
	 * 
	 * @param clazz
	 *            {@link Class} to scan.
	 * @return Set of all superclasses and interfaces of clazz which implement
	 *         {@link DataValue}.
	 */
	private Set<Class<? extends DataValue>> getDataValueSuperclassesAndInterfaces(
			Class<? extends DataValue> clazz) {
		final Set<Class<? extends DataValue>> superClasses = new HashSet<>();

		final Class<?> superClass = clazz.getSuperclass();
		if (superClass != null && DataValue.class.isAssignableFrom(superClass)
				&& superClass != DataValue.class) {
			superClasses.add((Class<? extends DataValue>) superClass);
			superClasses
					.addAll(getDataValueSuperclassesAndInterfaces((Class<? extends DataValue>) superClass));
		}

		for (Class<?> iface : clazz.getInterfaces()) {
			if (DataValue.class.isAssignableFrom(iface)
					&& iface != DataValue.class) {
				superClasses.add((Class<? extends DataValue>) iface);
				superClasses
						.addAll(getDataValueSuperclassesAndInterfaces((Class<? extends DataValue>) iface));
				break;
			}
		}

		return superClasses;
	}

	@Override
	public Collection<InputAdapter> getMatchingInputAdapters(
			final Class<? extends DataValue> dataValueClass) {
		if (m_pluginsByDataValue == null) {
			processInstances();
		}

		Set<InputAdapter> set = m_pluginsByDataValue.get(dataValueClass);

		if (set == null) {
			// check superclasses of dataValueClass
			for (Class<? extends DataValue> c : getDataValueSuperclassesAndInterfaces(dataValueClass)) {
				set = m_pluginsByDataValue.get(c);
				
				if (set != null) {
					// save result for dataValueClass
					m_pluginsByDataValue.put(dataValueClass, set);
					
					break;
				}
			}
			
		}

		return set;
	}

	/*
	 * Process the plugin instances (sort them into the m_pluginByDataValue map
	 * for faster access later on)
	 */
	private void processInstances() {
		m_pluginsByDataValue = new WeakHashMap<Class<? extends DataValue>, Set<InputAdapter>>();

		for (InputAdapter<?, ?> p : this.getInstances()) {
			Class<? extends DataValue> type = p.getInputType();

			Set<InputAdapter> set = m_pluginsByDataValue.get(type);

			if (set == null) {
				set = new TreeSet<InputAdapter>(Comparator.reverseOrder()); // automatically sorts by priority
				m_pluginsByDataValue.put(type, set);
			}

			set.add(p);
		}
	}
	
}
