package org.knime.knip.scijava.commands.mapping;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Service which handles {@link ColumnModuleItemMapping}s. They are used by
 * {@link ColumnInputMappingKnimePreprocessor} to determine which data table
 * column should fill the value of a module input.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 * @see ColumnModuleItemMapping
 * @see ColumnToModuleItemMappingService
 */
@Plugin(type = ColumnToModuleItemMappingService.class, priority = Priority.NORMAL_PRIORITY)
public class DefaultColumnToModuleItemMappingService
		extends AbstractColumnToModuleItemMappingService {
	// NB: Marker interface
}
