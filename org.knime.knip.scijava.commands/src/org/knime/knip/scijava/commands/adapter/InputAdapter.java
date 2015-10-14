
package org.knime.knip.scijava.commands.adapter;

import org.knime.core.data.DataValue;
import org.scijava.convert.Converter;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.SingletonPlugin;

/**
 * An InputAdapter adapts {@link DataValue} to another type.
 * <p>
 * Adapter plugins discoverable at runtime must implement this interface and be
 * annotated with @{@link Plugin} with attribute {@link Plugin#type()} =
 * {@link InputAdapterPlugin}.class.
 * </p>
 *
 * @param <T>
 *            type of the value to get from the DataValue.
 * @param <V>
 *            DataValue subclass which this adapter gets values from.
 * @author Jonathan Hale (University of Konstanz)
 * @author Christian Dietz, University of Konstanz
 *
 */
public interface InputAdapter<V extends DataValue, T>
		extends SingletonPlugin, Converter<V, T> {
}
