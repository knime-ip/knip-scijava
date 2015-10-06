package org.knime.knip.scijava.commands.adapter;

import org.knime.core.data.DataCell;
import org.scijava.convert.Converter;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.SingletonPlugin;

/**
 * A plugin which extends type to {@link DataCell} adapting capabilities.
 * <p>
 * Output adapter plugins discoverable at runtime must implement this interface
 * and be annotated with @{@link Plugin} with attribute {@link Plugin#type()} =
 * {@link OutputAdapterPlugin}.class.
 * </p>
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @see Plugin
 * @see InputAdapterService
 */
public interface OutputAdapterPlugin<T, C extends DataCell>
		extends SingletonPlugin, Converter<T, C>, OutputAdapter<T, C> {
	// Marker interface
}
