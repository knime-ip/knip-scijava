package org.knime.knip.scijava.commands.adapter;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;

/**
 * Typically an output adapter creates a Knime DataCell from a type instance.
 * 
 * @param T
 *            source type which this adapter creates cells from.
 * @param C
 *            DataCell subclass which is created by this adapter.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
public interface OutputAdapter<T, C extends DataCell> {
	/**
	 * Create a DataCell from an object.
	 * 
	 * @param o
	 *            the object to create the DataCell from.
	 * @return
	 */
	DataCell createCell(T o);

	/**
	 * Returns class of the type this adapter creates cells from.
	 * 
	 * @return Class<T> of the source type.
	 */
	Class<T> getSourceType();

	/**
	 * Returns class of the DataCell this adapter creates.
	 * 
	 * @return {@link DataType} type of the DataCell this adapter creates.
	 */
	DataType getDataCellType();
}
