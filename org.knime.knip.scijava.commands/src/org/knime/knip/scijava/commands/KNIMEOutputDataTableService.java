package org.knime.knip.scijava.commands;

import java.lang.ref.WeakReference;

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
public class KNIMEOutputDataTableService extends AbstractService
		implements OutputDataRowService {

	private WeakReference<DataContainer> m_dataContainer = new WeakReference<>(
			null);
	private WeakReference<DataRow> m_curRow = new WeakReference<>(null);

	/**
	 * Create a KnimeOutputDataTableService with given output DataTableSpec.
	 *
	 * @param container
	 *            DataTableSpec of the output table.
	 */
	public void setOutputContainer(final DataContainer container) {
		m_dataContainer = new WeakReference<>(container);
	}

	@Override
	public void setOutputDataRow(final DataRow r) {
		m_curRow = new WeakReference<>(r);
	}

	@Override
	public DataRow getOutputDataRow() {
		return m_curRow.get();
	}

	/**
	 * Appends currently set DataRow to the contained DataTable and sets the
	 * current row to null.
	 */
	public void appendRow() {
		if (m_dataContainer.get() != null) {
			m_dataContainer.get().addRowToTable(m_curRow.get());
		}
		m_curRow = new WeakReference<>(null);
	}

	/**
	 * @return the DataContainer set in the Constructor.
	 */
	public DataContainer getDataContainer() {
		return m_dataContainer.get();
	}

}
