package org.knime.scijava.commands.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;

import org.knime.scijava.commands.mapping.ColumnModuleItemMappingService.ColumnToModuleItemMappingChangeListener;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.service.AbstractService;

/**
 * Abstract implementation of ColumnModuleItemMappingService. Use the subclasses
 * {@link DefaultColumnToInputMappingService} or
 * {@link DefaultOutputToColumnMappingService} to which mark the use of the
 * contained mappings.
 *
 * @author Jonathan Hale (University of Konstanz)
 * @author Gabriel Einsdorf (University of Konstanz)
 */
abstract class AbstractColumnModuleItemMappingService extends AbstractService
		implements ColumnModuleItemMappingService,
		ColumnToModuleItemMappingChangeListener {

	private static final String INACTIVE = "INACTIVE_MAPPING";

	/** List that stores the order of the MappingIds */
	private final List<String> m_orderedMappingIds = new ArrayList<>();

	private final WeakHashMap<String, ColumnModuleItemMapping> m_mappings = new WeakHashMap<>();
	private final WeakHashMap<String, String> m_mappingIdsByItemName = new WeakHashMap<>();

	@Override
	public List<ColumnModuleItemMapping> getMappingsList() {
		return Collections
				.unmodifiableList(new ArrayList<>(m_mappings.values()));
	}

	@Override
	public void onMappingColumnChanged(
			final AbstractColumnModuleItemMappingService.ColumnToModuleItemMappingChangeEvent e) {
		// NB: nothing to do here currently.
	}

	@Override
	public void onMappingItemChanged(
			final AbstractColumnModuleItemMappingService.ColumnToModuleItemMappingChangeEvent e) {

		// free previously mapped value
		String prev = e.getPreviousValue();
		if(!INACTIVE.equals(prev)){
			m_mappingIdsByItemName.remove(e.getPreviousValue());
		}

		final ColumnModuleItemMapping sourceMapping = e.getSourceMapping();

		String itemName = sourceMapping.getItemName();

		if(INACTIVE.equals(itemName)){
			return;
		}

		// check if input was mapped before
		String otherMappingID = m_mappingIdsByItemName.get(itemName);
		if (otherMappingID != null) {
			// deactivate old mapping
			ColumnModuleItemMapping otherMapping = m_mappings
					.get(otherMappingID);
			otherMapping.setActive(false);
			// unlink the mapping
			m_mappingIdsByItemName.remove(itemName);
			otherMapping.setItemName(INACTIVE);

			// ensure new mapping is active
			sourceMapping.setActive(true);
		}
		// save new item mapping.
		m_mappingIdsByItemName.put(itemName, sourceMapping.getID());
	}

	@Override
	public void clear() {
		// remove all mappings

		m_mappings.clear();
		m_mappingIdsByItemName.clear();
		m_orderedMappingIds.clear();
	}

	@Override
	public void addMapping(final String columnName, final String inputName) {
		addMapping(columnName, inputName, true);
	}

	@Override
	public void addMapping(String columnName, String inputName,
			boolean active) {
		final ColumnModuleItemMapping mapping = new DefaultColumnToModuleItemMapping(
				columnName, inputName);
		setupMapping(active, mapping);
	}

	private void setupMapping(boolean active,
			final ColumnModuleItemMapping mapping) {
		mapping.addMappingChangeListener(this);
		mapping.setActive(active);

		String id = mapping.getID();
		m_mappings.put(id, mapping);
		m_mappingIdsByItemName.put(mapping.getItemName(), id);
		m_orderedMappingIds.add(id);
	}

	@Override
	public int numMappings() {
		return m_mappings.size();
	}

	@Override
	public void removeMappingsByPosition(int... rows) {
		// collect id's of maps to remove
		List<String> removeList = new ArrayList<>(rows.length);
		for (int i : rows) {
			String id = m_orderedMappingIds.get(i);
			removeList.add(id);
		}
		// remove the elements
		removeList.forEach((String id) -> removeMapping(id));
	}

	/**
	 * Remove a mapping
	 *
	 * @param id
	 *            the id of the mapping
	 * @return
	 */
	private ColumnModuleItemMapping removeMapping(String id) {
		ColumnModuleItemMapping mapping = m_mappings.remove(id);
		if (mapping == null) {
			// mapping was not found.
			return null;
		}
		m_orderedMappingIds.remove(id);
		m_mappingIdsByItemName.remove(mapping.getItemName());
		mapping.removeMappingChangeListener(this);
		return mapping;
	}

	@Override
	public void setColumnNameByPosition(int rowIndex, String value) {
		m_mappings.get(m_orderedMappingIds.get(rowIndex)).setColumnName(value);
	}

	@Override
	public void setActiveByPosition(int rowIndex, Boolean value) {
		m_mappings.get(m_orderedMappingIds.get(rowIndex)).setActive(value);
	}

	@Override
	public void setItemNameByPosition(int rowIndex, String value) {
		m_mappings.get(m_orderedMappingIds.get(rowIndex)).setItemName(value);
	}

	@Override
	public String getColumnNameByPosition(int rowIndex) {
		return m_mappings.get(m_orderedMappingIds.get(rowIndex))
				.getColumnName();
	}

	@Override
	public boolean isActiveByPosition(int rowIndex) {
		return m_mappings.get(m_orderedMappingIds.get(rowIndex)).isActive();
	}

	@Override
	public String getItemNameByPosition(int rowIndex) {
		return m_mappings.get(m_orderedMappingIds.get(rowIndex)).getItemName();
	}

	@Override
	public boolean isItemMapped(String itemName) {
		String id = m_mappingIdsByItemName.get(itemName);

		boolean out = id != null && m_mappings.get(id).isActive();
		return out;
	}

	@Override
	public String getColumnNameForInput(String inputName) {
		return m_mappings.get(m_mappingIdsByItemName.get(inputName))
				.getColumnName();
	}

	/**
	 * Default implementation of {@link ColumnModuleItemMapping}
	 *
	 * @author Jonathan Hale
	 */
	static final class DefaultColumnToModuleItemMapping
			implements ColumnModuleItemMapping {

		protected String m_columnName;
		protected String m_itemName;
		protected boolean m_active;
		protected ArrayList<ColumnToModuleItemMappingChangeListener> m_listeners;
		private String m_uuID;

		public DefaultColumnToModuleItemMapping(final String columnName,
				final String itemName) {
			m_columnName = columnName;
			m_itemName = itemName;
			m_active = true;
			m_listeners = new ArrayList<ColumnToModuleItemMappingChangeListener>();
			m_uuID = UUID.randomUUID().toString();
		}

		public DefaultColumnToModuleItemMapping(String columnName,
				String inputName, String id) {
			m_columnName = columnName;
			m_itemName = inputName;
			m_active = true;
			m_listeners = new ArrayList<ColumnToModuleItemMappingChangeListener>();
			m_uuID = id;
		}

		@Override
		public String getColumnName() {
			return m_columnName;
		}

		@Override
		public String getItemName() {
			return m_itemName;
		}

		@Override
		public ModuleItem<?> getModuleItem(final Module module) {
			return module.getInfo().getInput(m_itemName);
		}

		@Override
		public boolean isActive() {
			return m_active;
		}

		@Override
		public void setActive(final boolean flag) {
			m_active = flag;
		}

		@Override
		public void addMappingChangeListener(
				final ColumnToModuleItemMappingChangeListener listener) {
			m_listeners.add(listener);
		}

		@Override
		public void removeMappingChangeListener(
				final ColumnToModuleItemMappingChangeListener listener) {
			m_listeners.remove(listener);
		}

		@Override
		public void fireMappingColumnChanged(final String oldValue) {
			final AbstractColumnModuleItemMappingService.ColumnToModuleItemMappingChangeEvent e = new AbstractColumnModuleItemMappingService.ColumnToModuleItemMappingChangeEvent(
					this, oldValue);
			for (final ColumnToModuleItemMappingChangeListener l : m_listeners) {
				l.onMappingColumnChanged(e);
			}
		}

		@Override
		public void fireMappingItemChanged(final String oldValue) {
			final AbstractColumnModuleItemMappingService.ColumnToModuleItemMappingChangeEvent e = new AbstractColumnModuleItemMappingService.ColumnToModuleItemMappingChangeEvent(
					this, oldValue);
			for (final ColumnToModuleItemMappingChangeListener l : m_listeners) {
				l.onMappingItemChanged(e);
			}
		}

		@Override
		public void setColumnName(final String columnName) {
			if (columnName == null) {
				return;
			}
			if (!columnName.equals(m_columnName)) {
				final String oldName = m_columnName;
				m_columnName = columnName;
				fireMappingColumnChanged(oldName);
			}
		}

		@Override
		public void setItemName(final String itemName) {
			if (itemName == null) {
				return;
			}
			if (!itemName.equals(m_itemName)) {
				final String oldName = m_itemName;
				m_itemName = itemName;
				fireMappingItemChanged(oldName);
			}
		}

		@Override
		public String getID() {
			return m_uuID;
		}
	}

	/**
	 * Event for {@link ColumnModuleItemMapping} changes.
	 *
	 * @author Jonathan Hale (University of Konstanz)
	 */
	public static class ColumnToModuleItemMappingChangeEvent
			extends EventObject {

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

	@Override
	public String[] serialize() {
		List<String> out = new ArrayList<>();

		for (String id : m_orderedMappingIds) {
			ColumnModuleItemMapping m = m_mappings.get(id);
			out.add(m.getColumnName() + "\n" + m.getItemName() + "\n"
					+ m.getID() + "\n" + (m.isActive() ? "true" : "false"));
		}
		return out.toArray(new String[numMappings()]);
	}

	@Override
	public void deserialize(String[] serializedMappings) {
		for (final String s : serializedMappings) {
			final String[] names = s.split("\n");

			if (names.length != 4) {
				// Invalid format!
				throw new IllegalArgumentException(
						"Unable to deserialize settings: invalid amount of input tokens!");
			}

			/*
			 * format is [0] column name [1] module input name [2] id of the
			 * mapping [3] active, either "true" or "false"
			 */
			boolean active = names[3].equals("true");

			// recreate mapping with the deserialization constructor
			final ColumnModuleItemMapping mapping = new DefaultColumnToModuleItemMapping(
					names[0], names[1], names[2]);
			setupMapping(active, mapping);
		}
	}
}
