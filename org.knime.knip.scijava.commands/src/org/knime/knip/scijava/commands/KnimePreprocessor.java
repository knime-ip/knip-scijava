
package org.knime.knip.scijava.commands;

import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.Plugin;

/**
 * A preprocessor that handles unresolved parameters of various types using a
 * {@link KNIMEInputDataTableService}.
 * <p>
 * KnimePreprocessor plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link KnimePreprocessor}.class.
 * </p>
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
public interface KnimePreprocessor extends PreprocessorPlugin {
	// Marker interface
}
