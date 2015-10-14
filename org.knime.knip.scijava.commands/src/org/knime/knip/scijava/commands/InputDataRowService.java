package org.knime.knip.scijava.commands;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

/**
 * Service holding a DataRow for input.
 *
 * <p>
 * InputDataRowService plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link InputDataRowService}.class.
 * </p>
 *
 * @author Jonathan Hale (University of Konstanz)
 * @see OutputDataRowService
 */
public interface InputDataRowService extends Service {

	/**
	 * @return the contained DataRow.
	 */
	DataRow getInputDataRow();

	/**
	 * @return the data table spec for the contained DataRow.
	 */
	DataTableSpec getInputDataTableSpec();
}
