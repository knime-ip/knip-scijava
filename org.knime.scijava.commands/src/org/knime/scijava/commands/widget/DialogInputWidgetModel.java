package org.knime.scijava.commands.widget;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.scijava.widget.WidgetModel;

/**
 * Since KNIME does not provide an interface for DialogComponents, this
 * interface defines methods needed by DialogComponents for WidgetModels. Later
 * the WidgetModel can be used by a DialogComponent subclass which wrapps this
 * WidgetModel, calling exactly these defined methods.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
public interface DialogInputWidgetModel extends WidgetModel {

	/**
	 * Update the SettingsModel to reflect changes in the component.
	 */
	public void updateSettingsModel();

	/**
	 * Get the underlying SettingsModel
	 * 
	 * @return
	 */
	public SettingsModel getSettingsModel();

	/**
	 * Update to reflect changes in SettingsModel.
	 */
	public void updateToSettingsModel();

}