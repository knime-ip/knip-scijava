package org.knime.knip.scijava.commands.adapter;

import java.util.Collection;

import org.knime.core.data.DataType;
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
public interface InputAdapterService extends
		SingletonService<InputAdapterPlugin> {

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
			Class<D> dataValueClass, Class<T> valueClass);

	/**
	 * Convenience method to get all {@link InputAdapter}s which adapt a certain
	 * {@link DataType}.
	 * 
	 * @param type
	 *            the {@link DataType}
	 * @return a {@link Collection} of {@link InputAdapter} which adapt that
	 *         DataType
	 * 
	 * @see #getMatchingInputAdapters(Class)
	 */
	Collection<InputAdapterPlugin> getMatchingInputAdapters(DataType type);

	/**
	 * Get all {@link InputAdapter}s which adapt a certain {@link DataValue}.
	 * 
	 * @param dataValueClass
	 *            class of the DataValue which should be adapted
	 * @return a {@link Collection} of {@link InputAdapter} which adapt that
	 *         {@link DataValue}
	 */
	Collection<InputAdapterPlugin> getMatchingInputAdapters(
			Class<? extends DataValue> dataValueClass);

}
