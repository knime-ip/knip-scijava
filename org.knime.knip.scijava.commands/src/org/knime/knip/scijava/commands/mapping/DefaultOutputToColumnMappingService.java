package org.knime.knip.scijava.commands.mapping;

import org.scijava.plugin.Plugin;

/**
 * Default implementation of {@link OutputToColumnMappingService}.
 *
 * @author Jonathan Hale
 */
@Plugin(type = ColumnToInputMappingService.class)
public class DefaultOutputToColumnMappingService
		extends AbstractColumnModuleItemMappingService
		implements OutputToColumnMappingService {
	// Everything already implemented in superclass
}
