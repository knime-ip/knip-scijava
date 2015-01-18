package org.knime.knip.scijava.commands.adapter;

import org.knime.core.data.DataValue;

/**
 * An InputAdapter adapts {@link DataValue} to another type.
 * 
 * @param T
 *            type of the value to get from the DataValue.
 * @param V
 *            DataValue subclass which this adapter gets values from.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
public interface InputAdapter<V extends DataValue, T> {
	/**
	 * Get the value of a DataValue.
	 * 
	 * @param v
	 *            the DataValue to get the value from.
	 * @return a value of v.
	 */
	T getValue(V v);

	/**
	 * Returns class of the DataValue this adapter creates cells from.
	 * 
	 * @return Class<T> class of the source DataValue type.
	 */
	Class<V> getDataValueType();

	/**
	 * Returns class of the type this adapter creates.
	 * 
	 * @return Class<T> class of the type this adapter creates.
	 */
	Class<T> getType();
}
