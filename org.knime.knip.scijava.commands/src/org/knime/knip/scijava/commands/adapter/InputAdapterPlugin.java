package org.knime.knip.scijava.commands.adapter;

import org.knime.core.data.DataValue;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.SingletonPlugin;

/**
 * A plugin which extends {@link DataValue} adapting capabilities.
 * <p>
 * Adapter plugins discoverable at runtime must implement this interface and be
 * annotated with @{@link Plugin} with attribute {@link Plugin#type()} =
 * {@link InputAdapterPlugin}.class.
 * </p>
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @see Plugin
 * @see InputAdapterService
 */
public interface InputAdapterPlugin<V extends DataValue, T> extends
		SingletonPlugin, InputAdapter<V, T> {
}
