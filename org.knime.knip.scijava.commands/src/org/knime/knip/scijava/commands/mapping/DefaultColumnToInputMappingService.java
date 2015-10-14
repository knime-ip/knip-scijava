package org.knime.knip.scijava.commands.mapping;

import org.scijava.plugin.Plugin;

/**
 * Default implementation of {@link ColumnToInputMappingService}.
 *
 * @author Jonathan Hale
 */
@Plugin(type = ColumnToInputMappingService.class)
public class DefaultColumnToInputMappingService
		extends AbstractColumnModuleItemMappingService
		implements ColumnToInputMappingService {
	// Everything already implemented in superclass
}
