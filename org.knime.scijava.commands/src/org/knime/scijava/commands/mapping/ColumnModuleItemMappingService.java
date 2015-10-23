package org.knime.scijava.commands.mapping;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import org.knime.core.data.DataColumnSpec;
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
	 * Event for {@link ColumnModuleItemMapping} changes.
	 *
	 * @author Jonathan Hale (University of Konstanz)
	 */
	class ColumnToModuleItemMappingChangeEvent extends EventObject {

		/**
		 * Generated serialVersionID
		 */
		private static final long serialVersionUID = 8652877000556694115L;

		private final String oldValue;

		/**
		 * Constructor
		 *
		 * @param source
		 *            the changed {@link ColumnModuleItemMapping}
		 */
		public ColumnToModuleItemMappingChangeEvent(
				final ColumnModuleItemMapping source, final String oldValue) {
			super(source);

			this.oldValue = oldValue;
		}

		/**
		 * Get the changed {@link ColumnModuleItemMapping}.
		 *
		 * @return the changed mapping
		 */
		public ColumnModuleItemMapping getSourceMapping() {
			return (ColumnModuleItemMapping) source;
		}

		/**
		 * Get the previous value of the column/input name.
		 *
		 * @return the previous column or input name.
		 */
		public String getPreviousValue() {
			return oldValue;
		}
	}

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
		void onMappingColumnChanged(ColumnToModuleItemMappingChangeEvent e);

		/**
		 * Called when a {@link ColumnModuleItemMappingChangeEventDispatcher}
		 * this listener listens to fires a event when the item name changed.
		 *
		 * @param e
		 *            the event that has been fired.
		 */
		void onMappingItemChanged(ColumnToModuleItemMappingChangeEvent e);
	}

	/**
	 * Interface for classes which fire ColumnToModuleItemMappingChangeEvent
	 * events.
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
		 * Call
		 * {@link ColumnToModuleItemMappingChangeListener#onMappingColumnChanged(ColumnToModuleItemMappingChangeEvent)}
		 * on all listeners.
		 *
		 * @param oldValue
		 *            value which has been overwritten
		 */
		void fireMappingColumnChanged(String oldValue);

		/**
		 * Call
		 * {@link ColumnToModuleItemMappingChangeListener#onMappingItemChanged(ColumnToModuleItemMappingChangeEvent)}
		 * on all listeners.
		 *
		 * @param oldValue
		 *            value which has been overwritten
		 */
		void fireMappingItemChanged(String oldValue);
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
	 * Get a {@link ColumnModuleItemMapping} which maps the given columnName.
	 *
	 * @param columnName
	 *            name of the column to find a mapping for
	 * @return {@link ColumnModuleItemMapping} which maps columnName or null if
	 *         none could be found.
	 */
	ColumnModuleItemMapping getMappingForColumnName(String columnName);

	/**
	 * Get a {@link ColumnModuleItemMapping} which maps the given inputName.
	 *
	 * @param inputName
	 *            name of the input to find a mapping for
	 * @return {@link ColumnModuleItemMapping} which maps a column to inputName
	 *         or null if none could be found.
	 */
	ColumnModuleItemMapping getMappingForModuleItemName(String inputName);

	/**
	 * Get a {@link ColumnModuleItemMapping} which maps the given columns name.
	 *
	 * @param column
	 *            column which to find a mapping for
	 * @return {@link ColumnModuleItemMapping} which maps the columns name or
	 *         null if none could be found.
	 */
	ColumnModuleItemMapping getMappingForColumn(DataColumnSpec column);

	/**
	 * Get a {@link ColumnModuleItemMapping} which maps to the given items name.
	 *
	 * @param item
	 *            {@link ModuleItem} to find a mapping for
	 * @return {@link ColumnModuleItemMapping} wich maps top the given module
	 *         items name or null if none could be found.
	 */
	ColumnModuleItemMapping getMappingForModuleItem(ModuleItem<?> item);

	/**
	 * Add a {@link ColumnModuleItemMapping} from this Service if no such
	 * mapping exists yet.
	 *
	 * @param columnName
	 *            name of the column to map to inputName
	 * @param inputName
	 *            name of the input to map to
	 * @return created or existing {@link ColumnModuleItemMapping} mapping
	 *         columnName to inputName
	 */
	ColumnModuleItemMapping addMapping(String columnName, String inputName);

	/**
	 * Remove a {@link ColumnModuleItemMapping} from this Service.
	 *
	 * @param mapping
	 *            {@link ColumnModuleItemMapping} to remove
	 * @return removed {@link ColumnModuleItemMapping} or null if no such
	 *         mapping was found in this Service
	 */
	ColumnModuleItemMapping removeMapping(ColumnModuleItemMapping mapping);

	/**
	 * Remove all {@link ColumnModuleItemMapping}s from this Service.
	 */
	void clear();
}
