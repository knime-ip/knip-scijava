package org.knime.scijava.commands.adapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;

import org.knime.core.data.DataValue;
import org.scijava.plugin.AbstractSingletonService;
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
public class DefaultInputAdapterService extends
		AbstractSingletonService<InputAdapter>implements InputAdapterService {

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
			final Class<D> dataValueClass, final Class<T> valueClass) {
		if (m_pluginsByDataValue == null) {
			processInstances();
		}

		// go through all the plugins the the Set matching the dataValueClass
		final Collection<InputAdapter> plugins = getMatchingInputAdapters(
				dataValueClass);

		if (plugins == null) {
			// no adapters for dataValueClass
			return null;
		}

		for (final InputAdapter p : plugins) {
			if (valueClass.isAssignableFrom(p.getOutputType())) {
				// found a matching plugin
				return p;
			}
		}

		// none of the plugins matched
		return null;
	}

	/**
	 * Messy (TODO) method which recursively searches for all super classes
	 * which extends DataValue.
	 *
	 * @param clazz
	 *            {@link Class} to scan.
	 * @return Set of all superclasses and interfaces of clazz which implement
	 *         {@link DataValue}.
	 */
	private Set<Class<? extends DataValue>> getDataValueSuperclassesAndInterfaces(
			final Class<? extends DataValue> clazz) {
		final Set<Class<? extends DataValue>> superClasses = new HashSet<>();

		final Class<?> superClass = clazz.getSuperclass();
		if (superClass != null && DataValue.class.isAssignableFrom(superClass)
				&& superClass != DataValue.class) {
			superClasses.add((Class<? extends DataValue>) superClass);
			superClasses.addAll(getDataValueSuperclassesAndInterfaces(
					(Class<? extends DataValue>) superClass));
		}

		for (final Class<?> iface : clazz.getInterfaces()) {
			if (DataValue.class.isAssignableFrom(iface)
					&& iface != DataValue.class) {
				superClasses.add((Class<? extends DataValue>) iface);
				superClasses.addAll(getDataValueSuperclassesAndInterfaces(
						(Class<? extends DataValue>) iface));
				break;
			}
		}

		return superClasses;
	}

	@Override
	public Set<InputAdapter> getMatchingInputAdapters(
			final Class<? extends DataValue> dataValueClass) {
		if (m_pluginsByDataValue == null) {
			processInstances();
		}

		Set<InputAdapter> set = m_pluginsByDataValue.get(dataValueClass);

		if (set == null) {

			// check superclasses of dataValueClass
			for (final Class<? extends DataValue> c : getDataValueSuperclassesAndInterfaces(
					dataValueClass)) {
				set = m_pluginsByDataValue.get(c);

				if (set != null) {
					// save result for dataValueClass
					m_pluginsByDataValue.put(dataValueClass, set);
					break;
				}
			}
			if (set == null) { // datatype is unknown
				set = Collections.emptySet();
			}
		}
		return set;
	}

	/*
	 * Process the plugin instances (sort them into the m_pluginByDataValue map
	 * for faster access later on)
	 */
	private void processInstances() {
		m_pluginsByDataValue = new WeakHashMap<>();

		for (final InputAdapter<?, ?> p : this.getInstances()) {
			final Class<? extends DataValue> type = p.getInputType();

			Set<InputAdapter> set = m_pluginsByDataValue.get(type);

			if (set == null) {
				// automatically sorts by priority
				set = new TreeSet<>(Comparator.naturalOrder());
				m_pluginsByDataValue.put(type, set);
			}
			set.add(p);
		}
	}
}
