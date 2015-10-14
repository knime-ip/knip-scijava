package org.knime.knip.scijava.commands.settings;

import org.scijava.Priority;
import org.scijava.log.LogService;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.module.process.AbstractPreprocessorPlugin;
import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of NodeSettingsHarvester.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = PreprocessorPlugin.class, priority = Priority.NORMAL_PRIORITY)
public class DefaultNodeSettingsHarvester extends AbstractPreprocessorPlugin
		implements NodeSettingsHarvester {

	@Parameter
	NodeSettingsService m_settingsService;

	@Parameter
	LogService log;

	@Override
	public void process(final Module module) {
		for (final ModuleItem<?> input : module.getInfo().inputs()) {
			// shortcut to inputName
			final String inputName = input.getName();

			if (!module.isResolved(inputName)) {
				// we will not overwrite resolved input values

				// get settings for this input
				final Object value = m_settingsService.getValue(input);

				if (value != null) {
					// we have settings for this input
					module.setInput(inputName, value);
					module.setResolved(inputName, true);
				}
			}
		}
	}

}
