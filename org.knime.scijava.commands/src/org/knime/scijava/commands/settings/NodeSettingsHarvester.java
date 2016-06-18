package org.knime.scijava.commands.settings;

import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.Plugin;

/**
 * A preprocessor that handles unresolved parameters of various types using a
 * {@link NodeSettingsService}.
 *
 * <p>
 * NodeSettingsHarvester plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link NodeSettingsHarvester}.class.
 * </p>
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
public interface NodeSettingsHarvester extends PreprocessorPlugin {
	// NB: marker interface
}
