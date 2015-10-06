package org.knime.knip.scijava.commands.settings;

import java.util.Collection;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

/**
 * Interface for Services that provide functionality to create and modify
 * {@link SettingsModel}s.
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
	 * Set the value of the SettingsModel for a ModuleItem.
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
	 * Create a new SettingsModel for a ModuleItem.
	 * 
	 * @param moduleItem
	 *            ModuleItem to create a SettingsModel for.
	 * @return the created SettignsModel or null if no SettingsModel could be
	 *         created for moduleItem.
	 */
	SettingsModel createSettingsModel(ModuleItem<?> moduleItem);
	
	/**
	 * Create new SettingsModels for a Collection of ModuleItems.
	 * @param moduleItems
	 */
	Collection<SettingsModel> createSettingsModels(Iterable<ModuleItem<?>> moduleItems);

	/**
	 * @return all SettingsModels created by this NodeSettingsService.
	 */
	Collection<SettingsModel> getSettingsModels();

	/**
	 * TODO
	 * 
	 * @param settings
	 * @return
	 * @throws InvalidSettingsException
	 */
	boolean validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException;

	/**
	 * TODO
	 * 
	 * @param settings
	 * @return
	 * @throws InvalidSettingsException
	 */
	boolean loadSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException;

	/**
	 * TODO
	 * 
	 * @param settings
	 * @return
	 */
	boolean saveSettingsTo(NodeSettingsWO settings);

}
