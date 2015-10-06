package org.knime.knip.scijava.commands;

import org.knime.core.data.DataRow;
import org.knime.core.data.container.DataContainer;
import org.scijava.service.AbstractService;

/**
 * Implementation of OutputDataRowService allowing the output rows to be added
 * directly to a {@link DataContainer}.
 * 
 * This Service is not discoverable at runtime. Add it to a Context manually and
 * call {@link #setOutputContainer(DataContainer)} before first use.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
public class KnimeOutputDataTableService extends AbstractService implements
		OutputDataRowService {

	private DataContainer m_dataContainer;
	private DataRow m_curRow = null;

	/**
	 * Create a KnimeOutputDataTableService with given output DataTableSpec.
	 * 
	 * @param container
	 *            DataTableSpec of the output table.
	 */
	public void setOutputContainer(DataContainer container) {
		m_dataContainer = container;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOutputDataRow(DataRow r) {
		m_curRow = r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataRow getOutputDataRow() {
		return m_curRow;
	}

	/**
	 * Appends currently set DataRow to the contained DataTable and
	 * sets the current row to null.
	 */
	public void appendRow() {
		if (m_dataContainer != null) {
			m_dataContainer.addRowToTable(m_curRow);
		}
		m_curRow = null;
	}
	
	/**
	 * @return the DataContainer set in the Constructor.
	 */
	public DataContainer getDataContainer() {
		return m_dataContainer;
	}

}
