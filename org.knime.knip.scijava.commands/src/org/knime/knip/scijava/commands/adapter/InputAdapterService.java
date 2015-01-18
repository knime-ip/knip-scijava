package org.knime.knip.scijava.commands.adapter;

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
	 * @return an InputAdapter which adapts dataValueClass instances to
	 *         valueClass instances.
	 * 
	 */
	InputAdapter getMatchingInputAdapter(
			Class<? extends DataValue> dataValueClass, Class<?> valueClass);

}
