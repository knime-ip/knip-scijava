package org.knime.knip.scijava.commands;

import org.knime.core.data.DataRow;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

/**
 * Service holding a DataRow for output.
 * 
 * <p>
 * OutputDataRowService plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link OutputDataRowService}.class.
 * </p>
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @see InputDataRowService
 */
public interface OutputDataRowService extends Service {

	/**
	 * Set the contained DataRow.
	 * 
	 * @param r
	 *            the new DataRow.
	 */
	void setOutputDataRow(DataRow r);

	/**
	 * @returns the contained output DataRow.
	 */
	DataRow getOutputDataRow();

}
