package org.knime.scijava.commands.adapter;

import java.util.Collection;

import org.knime.core.data.DataValue;
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
	 * @param <T>
	 *            Type of the value, usually automatically deduced from the
	 *            parameters
	 * @return an InputAdapter which adapts dataValueClass instances to
	 *         valueClass instances.
	 *
	 */
	<D extends DataValue, T> InputAdapter getMatchingInputAdapter(
			Class<D> dataValueClass, Class<T> valueClass);

	/**
	 * Get all {@link InputAdapter}s which adapt a certain {@link DataValue}.
	 *
	 * @param dataValueClass
	 *            class of the DataValue which should be adapted
	 * @return a {@link Collection} of {@link InputAdapter} which adapt that
	 *         {@link DataValue}
	 */
	Collection<InputAdapter> getMatchingInputAdapters(
			Class<? extends DataValue> dataValueClass);

	/**
	 * Get the DataValue that is compatible with the given type. This method
	 * will only return DataValues for which an InputAdapter exists.
	 * 
	 * @param type
	 *            the type of the input
	 * @return a compatible DataValue
	 */
	Class<? extends DataValue> getMatchingInputValueClass(Class<?> type);
}
