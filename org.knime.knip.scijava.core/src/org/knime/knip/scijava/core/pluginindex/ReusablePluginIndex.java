package org.knime.knip.scijava.core.pluginindex;

import org.scijava.plugin.PluginFinder;
import org.scijava.plugin.PluginIndex;

/**
 * {@link PluginFinder} which can be reused without re-adding all plugins from a
 * {@link PluginFinder} to the index when creating a Context with this plugin
 * index.
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
public class ReusablePluginIndex extends PluginIndex {

	/* flag to keep track of whether #discover() has been called already */
	private boolean discovered = false;

	@Override
	public void discover() {
		if (!discovered) {
			super.discover();
		}
		discovered = true;
	}
}
