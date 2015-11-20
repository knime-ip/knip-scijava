package org.knime.scijava.commands.mapping;

import org.scijava.module.Module;

/**
 * Service which manages mappings used for mapping {@link Module} outputs to
 * columns.
 *
 * @author Jonathan Hale
 * @see
 */
public interface OutputToColumnMappingService
		extends ColumnModuleItemMappingService {
	// NB: Marker interface
}
