package org.knime.knip.scijava.commands.adapter;

import org.scijava.plugin.Plugin;
import org.scijava.plugin.SingletonService;

/**
 * Interface for OutputAdapter functionality.
 * 
 * <p>
 * OutputAdapterService plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link OutputAdapterService}.class.
 * </p>
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@SuppressWarnings("rawtypes")
public interface OutputAdapterService extends
		SingletonService<OutputAdapterPlugin> {

	/**
	 * Get a OutputAdapter which adapts valueClass to a DataCell.
	 * 
	 * @param valueClass
	 *            class of a value which needs to be adapted.
	 * @return a OutputAdapter which adapts valueClass instances to a DataCell.
	 */
	OutputAdapter getMatchingOutputAdapter(Class<?> valueClass);

}
