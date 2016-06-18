package org.knime.scijava.commands.settings;

import org.scijava.plugin.Plugin;

/**
 * Marker interface for the SettingsService that stores the settings of the node
 * dialog.
 *
 * <p>
 * NodeDialogSettingsService plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link NodeModelSettingsService}.class.
 * </p>
 *
 *
 * @author Gabriel Einsdorf (University of Konstanz)
 *
 */
public interface NodeModelSettingsService extends NodeSettingsService {
	// NB Marker Interface
}
