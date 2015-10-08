package org.knime.knip.scijava.commands.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Service which handles {@link ColumnModuleItemMapping}s. They are used by
 * {@link ColumnInputMappingKnimePreprocessor} to determine which data table
 * column should fill the value of a module input.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 * @see ColumnModuleItemMapping
 * @see ColumnToModuleItemMappingService
 */
@Plugin(type = ColumnToModuleItemMappingService.class, priority=Priority.NORMAL_PRIORITY)
public class DefaultColumnToModuleItemMappingService extends
		AbstractColumnToModuleItemMappingService {

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
	protected void addMapping(final ColumnModuleItemMapping mapping) {
		m_mappings.add(mapping);
		m_mappingsByColumn.put(mapping.getColumnName(), mapping);
		m_mappingsByItem.put(mapping.getItemName(), mapping);
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
}
