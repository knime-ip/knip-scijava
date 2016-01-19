package org.knime.scijava.commands.adapter;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataValue;
import org.scijava.convert.Converter;
import org.scijava.plugin.SingletonPlugin;

/**
 * Typically an output adapter creates a KNIME DataCell from a type instance.
 *
 * @param T
 *            source type which this adapter creates cells from.
 * @param C
 *            DataCell subclass which is created by this adapter.
 *
 * @author Jonathan Hale (University of Konstanz)
 * @author Christian Dietz, University of Konstanz
 *
 */
public interface OutputAdapter<T, C extends DataValue>
		extends SingletonPlugin, Converter<T, C> {
	// Marker interface
}
