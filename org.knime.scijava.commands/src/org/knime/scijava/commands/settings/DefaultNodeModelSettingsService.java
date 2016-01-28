package org.knime.scijava.commands.settings;

import org.scijava.plugin.Plugin;

/**
 * Marker class
 *
 * @author gabriel
 *
 */

@Plugin(type = NodeModelSettingsService.class)
public class DefaultNodeModelSettingsService extends
		AbstractDefaultSettingsService implements NodeModelSettingsService {
	// NB Marker class
}
