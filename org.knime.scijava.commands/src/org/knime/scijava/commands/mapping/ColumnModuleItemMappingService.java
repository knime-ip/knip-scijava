package org.knime.scijava.commands.mapping;

import java.util.EventListener;
import java.util.List;

import org.knime.core.data.DataTable;
import org.scijava.module.ModuleItem;
import org.scijava.service.Service;

/**
 * ColumnToInputMappingService provides information on how to map names of
 * {@link DataTable} columns to names of {@link ModuleItem}s.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
public interface ColumnModuleItemMappingService extends Service {

	/**
	 * Interface for classes listening to changes to
	 * {@link ColumnModuleItemMapping}s.
	 *
	 * @author Jonathan Hale (University of Konstanz)
	 *
	 */
	public interface ColumnToModuleItemMappingChangeListener
			extends EventListener {
		/**
		 * Called when a {@link ColumnModuleItemMappingChangeEventDispatcher}
		 * this listener listens to fires a event when the column name changed.
		 *
		 * @param e
		 *            the event that has been fired.
		 */
		void onMappingColumnChanged(
				AbstractColumnModuleItemMappingService.ColumnToModuleItemMappingChangeEvent e);

		/**
		 * Called when a {@link ColumnModuleItemMappingChangeEventDispatcher}
		 * this listener listens to fires a event when the item name changed.
		 *
		 * @param e
		 *            the event that has been fired.
		 */
		void onMappingItemChanged(
				AbstractColumnModuleItemMappingService.ColumnToModuleItemMappingChangeEvent e);
	}

	/**
	 * Get a list of all {@link ColumnModuleItemMapping}s currently contained in
	 * this Service.
	 *
	 * @return {@link List} containing {@link ColumnModuleItemMapping} of this
	 *         Service.
	 */
	List<ColumnModuleItemMapping> getMappingsList();

	/**
	 * Add an active mapping to this Service.
	 *
	 * @param columnName
	 *            name of the column to map to inputName
	 * @param inputName
	 *            name of the input to map to
	 */
	void addMapping(String columnName, String inputName);

	/**
	 * Add a mapping to this Service
	 *
	 * @param columnName
	 *            name of the column to map to inputName.
	 * @param inputName
	 *            name of the input to map to.
	 * @param active
	 *            if the added mapping is active.
	 * 
	 */
	void addMapping(String columnName, String inputName, boolean active);

	/**
	 * Remove all mappings from this Service.
	 */
	void clear();

	/**
	 * @return the number of mappings stored in this service.
	 */
	int numMappings();

	// Position based methods

	/**
	 * Removes all mappings at the given positions.
	 * 
	 * @param rows
	 *            the positions of the mappings to remove.
	 * @return the removed mappings.
	 */
	void removeMappingsByPosition(int... rows);

	/**
	 * @param rowIndex
	 *            the index of the row containing the mapping
	 * @param columnName
	 *            the column name.
	 */
	void setColumnNameByPosition(int rowIndex, String columnName);

	/**
	 * @param rowIndex
	 *            the index of the row containing the mapping
	 * @param value
	 *            the boolean containing the activity state.
	 */
	void setActiveByPosition(int rowIndex, Boolean value);

	/**
	 * Set the name of the mapped input item of the mapping at the given
	 * position.
	 * 
	 * @param rowIndex
	 *            the position of the mapping.
	 * @param itemName
	 *            the name of the item.
	 */
	void setItemNameByPosition(int rowIndex, String itemName);

	/**
	 * @param rowIndex
	 *            the position
	 * @return the name of the mapped column
	 * 
	 */
	String getColumnNameByPosition(int rowIndex);

	/**
	 * @param rowIndex
	 *            the position
	 * @return if the mapping at the given position is active.
	 */
	boolean isActiveByPosition(int rowIndex);

	/**
	 * 
	 * @param rowIndex
	 *            the position
	 * @return the name of the input of the mapping at the given position.
	 */
	String getItemNameByPosition(int rowIndex);

	/**
	 * 
	 * @param inputName
	 *            the name of the input
	 * @return true if there is an active mapping for this input.
	 */
	boolean isInputMapped(String inputName);

	/**
	 * 
	 * @param inputName
	 *            the name of the input
	 * @return the column mapped to this input
	 */
	String getColumnNameForInput(String inputName);

	/**
	 * @return a list which contains serialized mappings for storing in a
	 *         settings model.
	 */
	String[] serialize();

	/**
	 * Reads the mappings from the given array, adding them to the Service.
	 * 
	 * @param serializedMappings
	 *            mappings serialized with
	 *            {@link ColumnModuleItemMappingService#serialize()}.
	 * @return if the serialization was successful.
	 */
	void deserialize(String[] serializedMappings);
}
