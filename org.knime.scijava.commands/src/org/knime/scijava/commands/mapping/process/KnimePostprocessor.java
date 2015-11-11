package org.knime.scijava.commands.mapping.process;

import org.knime.core.data.DataRow;
import org.scijava.module.process.PostprocessorPlugin;
import org.scijava.plugin.Plugin;

/**
 * Creates a {@link DataRow} from output of a module after its execution.
 *
 * <p>
 * KnimePostprocessor plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link KnimePostprocessor}.class.
 * </p>
 *
 * @author Jonathan Hale (University of Konstanz)
 */
public interface KnimePostprocessor extends PostprocessorPlugin {
	// Marker interface
}
