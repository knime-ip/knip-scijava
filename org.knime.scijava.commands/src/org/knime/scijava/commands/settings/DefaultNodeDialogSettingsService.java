package org.knime.scijava.commands.settings;

import org.scijava.plugin.Plugin;

/**
 * Marker class  
 * @author gabriel
 *
 */

@Plugin(type=NodeDialogSettingsService.class)
public class DefaultNodeDialogSettingsService extends
		AbstractDefaultSettingsService implements NodeDialogSettingsService {
	// NB Marker class
}
