package org.knime.scijava.commands.mapping;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

/**
 * Utility methods for column input mappings.
 *
 * This class contains static methods to serialize and deserialize
 * {@link ColumnModuleItemMapping}s as {@link SettingsModelStringArray}.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
public class ColumnToModuleItemMappingUtil {

	/**
	 * Fill a SettingsModelStringArray with the contents of a
	 * ColumnToModuleItemMappingService. The mappings are stored as Strings in
	 * the format "columnName\nactive\ninputName".
	 *
	 * @param service
	 *            service to fill the model with
	 * @param model
	 *            model to fill
	 * @return model
	 */
	public static SettingsModelStringArray fillStringArraySettingsModel(
			final ColumnModuleItemMappingService service,
			final SettingsModelStringArray model) {
		final List<ColumnModuleItemMapping> mappings = service
				.getMappingsList();
		final ArrayList<String> out = new ArrayList<String>(mappings.size());

		// FIXME: still uses columnmapping
		for (final ColumnModuleItemMapping m : mappings) {
			out.add(m.getColumnName() + "\n" + (m.isActive() ? "true" : "false")
					+ "\n" + m.getItemName());
		}

		model.setStringArrayValue(out.toArray(new String[] {}));
		return model;
	}

	/**
	 * Add contents of a {@link SettingsModelStringArray} to a
	 * {@link ColumnModuleItemMappingService}.
	 *
	 * @param modelValue
	 *            value of the model to get the contents from
	 * @param service
	 *            service to add the contents of model to
	 * @return true on success, false if model contained a String not in the
	 *         format "columnName\nactive\ninputName".
	 */
	public static boolean fillColumnToModuleItemMappingService(
			final String[] modelValue,
			final ColumnModuleItemMappingService service) {
		for (final String s : modelValue) {
			final String[] names = s.split("\n");

			if (names.length != 3) {
				// Invalid format!
				return false;
			}

			/*
			 * format is [0] column name [1] active, either "true" or "false"
			 * [2] module input name
			 */
			boolean active = names[1].equals("true");
			service.addMapping(names[0], names[2], active);
		}
		// done, no problems
		return true;
	}
}
