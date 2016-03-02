package org.knime.scijava.commands.settings;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialog;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

/**
 * Interface for Services that provide functionality to create and modify
 * {@link SettingsModel}s. It holds a {@link Map} of {@link SettingsModel}s with
 * a {@link WeakReference} to manage the SettingsModels for a {@link NodeModel}
 * or {@link NodeDialog}, but not prevent the {@link SettingsModel}s to be
 * garbage collected when the NodeModel or NodeDialog are destroyed.
 *
 * <p>
 * NodeSettingsService plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link NodeSettingsService}.class.
 * </p>
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
public interface NodeSettingsService extends Service {

	/**
	 * Set the value of the SettingsModel for a ModuleItem. Prints a warning if
	 * no SettingsModel exists for the passed <code>moduleItem</code>.
	 *
	 * @param moduleItem
	 *            name of the SettingsModel
	 * @param value
	 *            value to set the SettingsModels value to.
	 */
	void setValue(ModuleItem<?> moduleItem, Object value);

	/**
	 * Get the value of the SettingsModel of the given ModuleItem.
	 *
	 * @param moduleItem
	 * @return the value of the found SettingsModel.
	 */
	Object getValue(ModuleItem<?> moduleItem);

	/**
	 * Create a new {@link SettingsModel} for a {@link ModuleItem} and add it to
	 * the {@link Map} of {@link SettingsModel}s set via
	 * {@link #setSettingsModels(Map)}.
	 *
	 * @param moduleItem
	 *            ModuleItem to create a SettingsModel for.
	 * @return the created SettignsModel or null if no SettingsModel could be
	 *         created for moduleItem.
	 * @see #createSettingsModel(ModuleItem)
	 * @see #createSettingsModels(Iterable)
	 */
	SettingsModel createAndAddSettingsModel(ModuleItem<?> moduleItem,
			boolean forceColumnSelection);

	/**
	 * Create new {@link SettingsModel}s for ModuleItems and add them to the
	 * {@link Map} set via {@link #setSettingsModels(Map)}.
	 *
	 * @param moduleItems
	 * @return the created SettingsModels
	 */
	List<SettingsModel> createAndAddSettingsModels(
			Iterable<ModuleItem<?>> moduleItems);

	/**
	 * Validate all settingsModels in this service.
	 *
	 * @param settings
	 * @return <code>true</code> on success
	 * @throws InvalidSettingsException
	 */
	boolean validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException;

	/**
	 * Load settings in this service from <code>settings</code>.
	 *
	 * @param settings
	 *            Settings to load from
	 * @param tolerant
	 *            Whether to tolerate missing values
	 * @return <code>true</code> on success
	 * @throws InvalidSettingsException
	 */
	boolean loadSettingsFrom(NodeSettingsRO settings, boolean tolerant)
			throws InvalidSettingsException;

	/**
	 * Save settings in this service to <code>settings</code>.
	 *
	 * @param settings
	 * @return <code>true</code> on success
	 */
	boolean saveSettingsTo(NodeSettingsWO settings);

	/**
	 * Removes all settings from the Service.
	 */
	void clear();

	/**
	 * Removes the settingsModel associated with the given module item (input).
	 *
	 * @param item
	 *            the item which settings model will be removed.
	 */
	void removeSettingsModel(ModuleItem<?> item);
}
