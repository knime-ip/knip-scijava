package org.knime.knip.scijava.commands.settings.impl;

import org.knime.knip.scijava.commands.impl.DefaultKnimePreprocessor;
import org.knime.knip.scijava.commands.settings.NodeSettingsHarvester;
import org.knime.knip.scijava.commands.settings.NodeSettingsService;
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
@Plugin(type = PreprocessorPlugin.class, priority = DefaultKnimePreprocessor.PRIORITY + 1.0)
public class DefaultNodeSettingsHarvester extends AbstractPreprocessorPlugin
		implements NodeSettingsHarvester {

	@Parameter
	NodeSettingsService m_settingsService;

	@Parameter
	LogService log;

	@Override
	public void process(Module module) {
		for (ModuleItem<?> i : module.getInfo().inputs()) {
			// shortcut to inputName
			String inputName = i.getName();

			if (!module.isResolved(inputName)) {
				// we will not overwrite resolved input values
				
				// get settings for this input
				Object value = m_settingsService.getValue(i);

				if (value != null) {
					// we have settings for this input
					module.setInput(inputName, value);
					module.setResolved(inputName, true);
				}
			}
		}
	}

}
