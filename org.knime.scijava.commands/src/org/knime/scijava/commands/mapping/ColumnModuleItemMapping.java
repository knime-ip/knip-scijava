package org.knime.scijava.commands.mapping;

import org.scijava.module.Module;
import org.scijava.module.ModuleItem;

/**
 * Interfaces for classes containing a mapping from column name to
 * {@link ModuleItem} name. This may be a column mapped to an input of a
 * {@link Module} or a {@link Module} output mapped to a column.
 *
 * @author Jonathan Hale (University of Konstanz)
 * @see ColumnModuleItemMappingService
 * @see ColumnToModuleItemMappingUtil
 */
public interface ColumnModuleItemMapping
		extends ColumnModuleItemMappingChangeEventDispatcher {
	/**
	 * Get column name.
	 *
	 * @return name of the column which is mapped
	 */
	String getColumnName();

	/**
	 * Get module item name.
	 *
	 * @return name of the {@link ModuleItem} which is mapped
	 */
	String getItemName();

	/**
	 * Get the {@link ModuleItem} with name contained by this mapping.
	 *
	 * @param module
	 *            the module to find the {@link ModuleItem} in
	 * @return {@link ModuleItem} with the contained name or null if module does
	 *         not have a item with that name.
	 */
	ModuleItem<?> getModuleItem(Module module);

	/**
	 * Check if this column to module item mapping is currently active.
	 *
	 * @return true if active, false otherwise.
	 */
	boolean isActive();

	/**
	 * Set this mapping as active or inactive. Inactive columns input Mappings
	 * should usually not be used by other services.
	 *
	 * @param flag
	 *            set to <code>true</code> to activate or <code>false</code> to
	 *            deactivate this mapping.
	 */
	void setActive(boolean flag);

	/**
	 * Set this mappings column name and update listeners if changed. Needs to
	 * call {@link #fireMappingColumnChanged(String)}.
	 *
	 * @param columnName
	 *            name of the column to set to
	 */
	void setColumnName(String columnName);

	/**
	 * Set this mappings item name and update listeners if changed. Needs to
	 * call {@link #fireMappingColumnChanged(String)}.
	 *
	 * @param itemName
	 *            name of the item to set to.
	 */
	void setItemName(String itemName);

	/**
	 * @return The universally unique ID of this mapping.
	 */
	String getID();

}