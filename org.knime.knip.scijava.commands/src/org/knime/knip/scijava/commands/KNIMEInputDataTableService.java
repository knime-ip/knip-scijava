package org.knime.knip.scijava.commands;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.scijava.service.AbstractService;

/**
 * InputDataRowService implementation as iterator over a DataTable.
 * 
 * This Service is not discoverable at runtime. Add it to the Context manually
 * and call either {@link #setInputDataTable(DataTable)} or
 * {@link #setInputDataTableIterator(RowIterator)} before use.
 * 
 * Be aware that {@link#getDataRow()} returns <code>null</code> if
 * {@link #next()} hasn't been called yet and if {@link #next()} was called
 * although {@link #hasNext()} returned false. this service!
 * 
 * This service only holds {@link WeakReference}s to the {@link DataTableSpec},
 * {@link RowIterator} and {@link DataRow} to ensure those objects can be
 * garbage collected once they are not referenced outside of this service.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
public class KNIMEInputDataTableService extends AbstractService
		implements InputDataRowService, Iterator<DataRow> {

	private WeakReference<DataTableSpec> m_tableSpec = new WeakReference<>(
			null);
	private WeakReference<RowIterator> m_rowItor = new WeakReference<>(null);
	private WeakReference<DataRow> m_curRow = new WeakReference<>(null);

	/**
	 * Set input DataTable
	 * 
	 * @param inData
	 *            DataTable to iterate over.
	 */
	public void setInputDataTable(DataTable inData) {
		if (inData == null) {
			setInputDataTableIterator(null);
			m_tableSpec = new WeakReference<>(null);
		} else {
			setInputDataTableIterator(inData.iterator());
			m_tableSpec = new WeakReference<>(inData.getDataTableSpec());
		}
	}

	/**
	 * Set input DataTable RowIterator
	 * 
	 * The input iterator continues at its current position and changes the
	 * given iterator. Also, the current DataRow returned by
	 * {@link #getInputDataRow()} will remain <code>null</code> until the next
	 * call of {@link #next()}.
	 * 
	 * @param inItor
	 *            RowIterator to get DataRows from.
	 */
	public void setInputDataTableIterator(RowIterator inItor) {
		m_rowItor = new WeakReference<>(inItor);
		m_curRow = new WeakReference<>(null);
	}

	@Override
	public DataRow getInputDataRow() {
		return m_curRow.get();
	}

	@Override
	public DataRow next() {
		if (m_rowItor.get() == null) {
			return null;
		}

		try {
			m_curRow = new WeakReference<>(m_rowItor.get().next());
		} catch (NoSuchElementException e) {
			m_curRow = new WeakReference<>(null);
			throw e;
		}

		return m_curRow.get();
	}

	@Override
	public boolean hasNext() {
		if (m_rowItor.get() == null) {
			return false;
		}
		return m_rowItor.get().hasNext();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException(
				"KnimeInputDataTableService may not alter its DataTable.");
	}

	@Override
	public DataTableSpec getInputDataTableSpec() {
		return m_tableSpec.get();
	}

}
