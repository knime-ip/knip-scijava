package org.knime.scijava.commands.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.scijava.commands.mapping.ColumnModuleItemMappingService.ColumnToModuleItemMappingChangeListener;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.service.AbstractService;

/**
 * Default implementation of ColumnModuleItemMappingService. Usually this class
 * should not be instantiated on its own. Instead use
 * {@link DefaultColumnToInputMappingService} or
 * {@link DefaultOutputToColumnMappingService} which define the use of the
 * contained mappings.
 *
 * Although this class implements all the methods declared by its interfaces,
 * this class is named "Abstract" to make aware of the above note, while not
 * restricting uses on its own without the mentioned subclasses.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
public class AbstractColumnModuleItemMappingService extends AbstractService
		implements ColumnModuleItemMappingService,
		ColumnToModuleItemMappingChangeListener {

	/** list containing all mappings of this service */
	private final ArrayList<ColumnModuleItemMapping> m_mappings = new ArrayList<ColumnModuleItemMapping>();

	/** mappings optimized for {@link #getMappingForColumnName(String)} */
	private final WeakHashMap<String, ColumnModuleItemMapping> m_mappingsByColumn = new WeakHashMap<String, ColumnModuleItemMapping>();
	private final WeakHashMap<String, ColumnModuleItemMapping> m_mappingsByItem = new WeakHashMap<String, ColumnModuleItemMapping>();

	@Override
	public List<ColumnModuleItemMapping> getMappingsList() {
		return m_mappings;
	}

	@Override
	public ColumnModuleItemMapping getMappingForColumnName(
			final String columnName) {
		return m_mappingsByColumn.get(columnName);
	}

	@Override
	public ColumnModuleItemMapping getMappingForModuleItemName(
			final String inputName) {
		return m_mappingsByItem.get(inputName);
	}

	@Override
	public ColumnModuleItemMapping removeMapping(
			final ColumnModuleItemMapping mapping) {

		if (m_mappings.remove(mapping)) {
			// a mapping has been removed, we need to update the hash maps
			m_mappingsByColumn.remove(mapping.getColumnName());
			m_mappingsByItem.remove(mapping.getItemName());

			mapping.removeMappingChangeListener(this);

			return mapping;
		}

		// given mapping was not found
		return null;
	}

	@Override
	public void onMappingColumnChanged(
			final ColumnToModuleItemMappingChangeEvent e) {
		// a column name has changed, we need to update the has maps to reflect
		// that change
		m_mappingsByColumn.remove(e.getPreviousValue());

		final ColumnModuleItemMapping mapping = e.getSourceMapping();
		m_mappingsByColumn.put(mapping.getColumnName(), mapping);
	}

	@Override
	public void onMappingItemChanged(
			final ColumnToModuleItemMappingChangeEvent e) {
		// a module input name has changed, we need to update the has maps to
		// reflect
		// that change
		m_mappingsByItem.remove(e.getPreviousValue());

		final ColumnModuleItemMapping mapping = e.getSourceMapping();
		m_mappingsByItem.put(mapping.getItemName(), mapping);
	}

	@Override
	public void clear() {
		// remove all mappings

		m_mappings.clear();
		m_mappingsByColumn.clear();
		m_mappingsByItem.clear();
	}

	@Override
	public ColumnModuleItemMapping getMappingForColumn(
			final DataColumnSpec column) {
		return getMappingForColumnName(column.getName());
	}

	@Override
	public ColumnModuleItemMapping getMappingForModuleItem(
			final ModuleItem<?> item) {
		return getMappingForModuleItemName(item.getName());
	}

	@Override
	public ColumnModuleItemMapping addMapping(final String columnName,
			final String itemName) {
		final ColumnModuleItemMapping m = new DefaultColumnToModuleItemMapping(
				columnName, itemName);
		m.addMappingChangeListener(this);
		addMapping(m);

		return m;
	}

	/**
	 * Add a pre created {@link ColumnModuleItemMapping} to the Service. This
	 * method is called by {@link #addMapping(String, String)}.
	 *
	 * @param mapping
	 *            {@link ColumnModuleItemMapping} to add
	 */
	protected void addMapping(final ColumnModuleItemMapping mapping) {
		m_mappings.add(mapping);
		m_mappingsByColumn.put(mapping.getColumnName(), mapping);
		m_mappingsByItem.put(mapping.getItemName(), mapping);
	}

	/**
	 * Default implementation of {@link ColumnModuleItemMapping}
	 *
	 * @author Jonathan Hale
	 */
	public static final class DefaultColumnToModuleItemMapping
			implements ColumnModuleItemMapping {

		protected String m_columnName;
		protected String m_itemName;
		protected boolean m_active;
		protected ArrayList<ColumnToModuleItemMappingChangeListener> m_listeners;

		public DefaultColumnToModuleItemMapping(final String columnName,
				final String itemName) {
			m_columnName = columnName;
			m_itemName = itemName;
			m_active = true;
			m_listeners = new ArrayList<ColumnToModuleItemMappingChangeListener>();
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
		public DataColumnSpec getColumnSpec(final DataTableSpec spec) {
			return spec.getColumnSpec(m_columnName);
		}

		@Override
		public Integer getColumnIndex(final DataTableSpec spec) {
			return spec.findColumnIndex(m_columnName);
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
			final ColumnToModuleItemMappingChangeEvent e = new ColumnToModuleItemMappingChangeEvent(
					this, oldValue);
			for (final ColumnToModuleItemMappingChangeListener l : m_listeners) {
				l.onMappingColumnChanged(e);
			}
		}

		@Override
		public void fireMappingItemChanged(final String oldValue) {
			final ColumnToModuleItemMappingChangeEvent e = new ColumnToModuleItemMappingChangeEvent(
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

	}

}
