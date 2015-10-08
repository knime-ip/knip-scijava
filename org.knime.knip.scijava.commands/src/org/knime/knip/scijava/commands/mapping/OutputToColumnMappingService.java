package org.knime.knip.scijava.commands.mapping;

import org.scijava.module.Module;

/**
 * Service which manages {@link ColumnModuleItemMapping}s used for mapping
 * {@link Module} outputs to columns.
 * 
 * @author Jonathan Hale
 * @see 
 */
public interface OutputToColumnMappingService
		extends ColumnToModuleItemMappingService {
	// NB: Marker interface
}
