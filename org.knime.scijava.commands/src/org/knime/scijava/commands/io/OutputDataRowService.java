package org.knime.scijava.commands.io;

import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.RowKey;
import org.scijava.module.Module;
import org.scijava.service.Service;

/**
 * Service holding a {@link DataCell}s output by a {@link Module}.
 *
 * @author Jonathan Hale (University of Konstanz)
 * @see InputDataRowService
 */
public interface OutputDataRowService extends Service {

	/**
	 * Set the contained DataRow.
	 *
	 * @param r
	 *            the Cells.
	 */
	void setOutputCells(List<DataCell> cells);

	/**
	 * @param key
	 *            RowKey for the created row
	 * @returns a DataRow from contained DataCells or <code>null</code> if no
	 *          cells were set.
	 */
	DataRow createOutputDataRow(RowKey key);

	/**
	 * Returns a reference to the contained list of DataCells.
	 *
	 * @return the output {@link DataCell}s or an empty array if no cells were
	 *         set.
	 */
	DataCell[] getOutputDataCells();

	/**
	 * Clear the current contained DataCells.
	 */
	void clear();

}
