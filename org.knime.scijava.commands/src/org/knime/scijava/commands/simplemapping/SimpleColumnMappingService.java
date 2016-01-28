package org.knime.scijava.commands.simplemapping;

import java.util.List;
import java.util.NoSuchElementException;

import org.scijava.service.Service;

/**
 * A simple service that stores a mapping between a modules input items and a
 * columns of a table.
 *
 * @author Gabriel Einsdorf
 */
public interface SimpleColumnMappingService extends Service {

	/**
	 * Returns the column mapped to the input with the given name.
	 *
	 * @param input
	 *            the name of the input
	 * @return the column mapped to the input or <code>null</code> if no column
	 *         is mapped to it.
	 */
	public String getMappedColumn(String input);

	/**
	 * Equivalent to {@link #getMappedColumn(String)} but throws a
	 * {@link NoSuchElementException} if no mapping is available.
	 *
	 * @param input
	 *            the name of the input
	 * @return the column mapped to the input.
	 * @throws NoSuchElementException
	 *             if there is no mapping for that input.
	 */
	public String mappedColumn(String input);

	/**
	 * Map the name of the input to the column name. If the input was mapped to
	 * a another column, that mapping is replaced by the new one.
	 *
	 * @param input
	 *            the name of the input
	 * @param column
	 *            the mapped column
	 */
	public void setMappedColumn(String input, String column);

	/**
	 * @return A list of all inputs that are mapped
	 */
	public List<String> getMappedInputs();

	/**
	 * Remove all mappings from this service.
	 */
	public void clear();

	/**
	 * @return a list which contains serialized mappings for storing in a
	 *         settings model.
	 */
	public String[] serialize();

	/**
	 * Reads the mappings from the given array, adding them to the Service.
	 * Replaces all current mappings in the Service.
	 *
	 * @param serializedMappings
	 *            mappings serialized with {@link #serialize()}.
	 * @return if the serialization was successful.
	 */
	public void deserialize(String[] serializedMappings);

}
