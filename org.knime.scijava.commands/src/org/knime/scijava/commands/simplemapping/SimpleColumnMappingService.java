package org.knime.scijava.commands.simplemapping;

import java.util.List;
import java.util.NoSuchElementException;

import org.scijava.service.Service;

/**
 * @author gabriel A simple service that stores a mapping between a modules
 *         input items and a colums of a table.
 *
 */
public interface SimpleColumnMappingService extends Service {

	/**
	 *
	 * @param input
	 *            the name of the input
	 * @return the column mapped to the input or <code>null</code> if no column
	 *         is mapped to it.
	 */
	public String getMappedColumn(String input);

	/**
	 * Like {@link SimpleColumnMappingService#getMappedColumn(String)} but
	 * throws a {@link NoSuchElementException} if no mapping is available.
	 *
	 * @param input the name of the input
	 * @return the column mapped to the input.
	 * @throws NoSuchElementException if there is no mapping for that input.
	 */
	public String mappedColumn(String input);

	/**
	 * maps an input to a column.
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

	public void clear();

}
