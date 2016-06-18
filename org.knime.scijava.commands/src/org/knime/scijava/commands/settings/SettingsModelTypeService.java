package org.knime.scijava.commands.settings;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.SingletonService;

/**
 * Interface for SettingsModelType functionality.
 *
 * <p>
 * SettingsModelTypeService plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link SettingsModelTypeService}.class.
 * </p>
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@SuppressWarnings("rawtypes")
public interface SettingsModelTypeService
		extends SingletonService<SettingsModelTypePlugin> {

	/**
	 * Get a SettingModelType which can set and get values from the specified
	 * SettingsModel.
	 *
	 * @param settingsModel
	 *            SettingsModel to find a SettingsModelType for.
	 * @return a SettingsModelType which can set/get values from settingsModel
	 *         or null if none could be found or <code>settingsModel</code> was
	 *         <code>null</code>.
	 */
	SettingsModelType getSettingsModelTypeFor(SettingsModel settingsModel);

	/**
	 * Get a SettingsModelType which can assign the given value to a
	 * SettingsModel.
	 *
	 * @param value
	 *            the value which to store in a SettingsModel.
	 * @return SettingsModelType which stores values of given value type.
	 */
	SettingsModelType getSettingsModelTypeFor(Class<?> value);

}
