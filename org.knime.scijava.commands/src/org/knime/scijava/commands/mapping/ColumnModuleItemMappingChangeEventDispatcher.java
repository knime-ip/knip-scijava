package org.knime.scijava.commands.mapping;

import org.knime.scijava.commands.mapping.ColumnModuleItemMappingService.ColumnToModuleItemMappingChangeListener;

/**
 * Interface for classes which fire ColumnToModuleItemMappingChangeEvent events.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
public interface ColumnModuleItemMappingChangeEventDispatcher {

	/**
	 * Add a listener to dispatch events to.
	 *
	 * @param listener
	 *            the listener to add
	 */
	void addMappingChangeListener(
			ColumnToModuleItemMappingChangeListener listener);

	/**
	 * Remove a listener from this dispatcher.
	 *
	 * @param listener
	 *            the listener to remove
	 */
	void removeMappingChangeListener(
			ColumnToModuleItemMappingChangeListener listener);

	/**
	 * Notify all listener that the column mapping has changed.
	 *
	 * @param oldValue
	 *            value which has been overwritten
	 */
	void fireMappingColumnChanged(String oldValue);

	/**
	 * Notify all listener that the item mapping has changed.
	 *
	 * @param oldValue
	 *            value which has been overwritten
	 */
	void fireMappingItemChanged(String oldValue);
}