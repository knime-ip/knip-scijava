package org.knime.scijava.commands.settings;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.SingletonPlugin;

/**
 * A plugin which extends {@link SettingsModel} adapting capabilities.
 *
 * <p>
 * SettingsModelTypePlugin plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link SettingsModelTypePlugin}.class.
 * </p>
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 * @param <T>
 * @param <V>
 */
public interface SettingsModelTypePlugin<T extends SettingsModel, V>
		extends SettingsModelType<T, V>, SingletonPlugin {
	// Marker interface
}
