package org.knime.knip.scijava.commands.widget;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.scijava.widget.InputWidget;

/**
 * Interface for InputWidgets with SettingsModels.
 * 
 * @author Jonathan Hale (University of Konstanz)
 *
 * @param <T>
 * @param <W>
 */
public interface DialogInputWidget<T, W> extends InputWidget<T, W> {
	
	/**
	 * Get the underlying SettingsModel.
	 * @return the SettingsModel.
	 */
	SettingsModel getSettingsModel();
	
	/**
	 * Update Widget to reflect current state of SettingsModel.
	 */
	void updateToSettingsModel();
	
}
