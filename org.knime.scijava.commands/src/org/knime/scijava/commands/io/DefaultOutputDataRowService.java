package org.knime.scijava.commands.io;

import java.lang.ref.WeakReference;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

/**
 * Default implementation of OutputDataRowService. Holds a {@link WeakReference}
 * to a {@link DataRow} to ensure that it can be garbage collected once the
 * {@link DataRow} is not referenced outside of this service.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = OutputDataRowService.class, priority = DefaultOutputDataRowService.PRIORITY)
public class DefaultOutputDataRowService extends AbstractService
		implements OutputDataRowService {

	/**
	 * Priority of this {@link Plugin}
	 */
	public static final double PRIORITY = Priority.NORMAL_PRIORITY;

	private WeakReference<List<DataCell>> m_cells = new WeakReference<>(null);

	@Override
	public void setOutputCells(final List<DataCell> cells) {
		m_cells = new WeakReference<>(cells);
	}

	@Override
	public DataRow createOutputDataRow(final RowKey key) {
		if (m_cells.get() == null) {
			return null;
		}
		return new DefaultRow(key, m_cells.get());
	}

	@Override
	public DataCell[] getOutputDataCells() {
		if (m_cells.get() == null) {
			return new DataCell[] {};
		}

		return m_cells.get().toArray(new DataCell[] {});
	}

	@Override
	public void clear() {
		m_cells = new WeakReference<>(null);
	}

}
