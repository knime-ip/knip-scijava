package org.knime.scijava.commands.mapping;

import org.knime.scijava.commands.mapping.process.ColumnInputMappingKnimePreprocessor;
import org.scijava.module.Module;

/**
 * Service which manages mappings used for mapping {@link Module} outputs to
 * columns.
 *
 * @author Jonathan Hale
 * @see ColumnInputMappingKnimePreprocessor
 */
public interface ColumnToInputMappingService
		extends ColumnModuleItemMappingService {
	// NB: Marker interface
}
