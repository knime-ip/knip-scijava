package org.knime.knip.scijava.commands;

import org.knime.core.data.DataValue;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.SingletonService;

/**
 * Interface for InputAdapter functionality.
 * 
 * <p>
 * InputAdapterService plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link InputAdapterService}.class.
 * </p>
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@SuppressWarnings("rawtypes")
public interface InputAdapterService extends SingletonService<InputAdapter> {

	/**
	 * Get an InputAdapter which adapts dataValueClass to valueClass.
	 * 
	 * @param dataValueClass
	 *            {@link DataValue} to adapt from.
	 * @param valueClass
	 *            value type to adapt to.
	 * 
	 * @param <D>
	 *            Type of the DataValue, usually automatically deduced from the
	 *            parameters
	 * @param <D>
	 *            Type of the value, usually automatically deduced from the
	 *            parameters
	 * @return an InputAdapter which adapts dataValueClass instances to
	 *         valueClass instances.
	 * 
	 */
	<D extends DataValue, T> InputAdapter getMatchingInputAdapter(
			final DataValue dataValueClass, Class<T> valueClass);

}
