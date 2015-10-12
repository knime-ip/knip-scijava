package org.knime.knip.scijava.commands;

import java.lang.ref.WeakReference;

import org.knime.core.data.DataRow;
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

	private WeakReference<DataRow> m_row = new WeakReference<>(null);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOutputDataRow(DataRow r) {
		m_row = new WeakReference<>(r);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataRow getOutputDataRow() {
		return m_row.get();
	}

}
