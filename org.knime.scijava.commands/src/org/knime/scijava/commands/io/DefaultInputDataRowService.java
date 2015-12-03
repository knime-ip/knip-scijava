package org.knime.scijava.commands.io;

import java.lang.ref.WeakReference;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

/**
 * Default implementation of InputDataRowService. Holds a {@link DataRow} and a
 * {@link DataTableSpec} via a {@link WeakReference} to ensure that they can be
 * garbage collected once they are not referenced outside this Service.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = InputDataRowService.class, priority = DefaultInputDataRowService.PRIORITY)
public class DefaultInputDataRowService extends AbstractService
		implements InputDataRowService {

	/**
	 * Priority of this {@link Plugin}
	 */
	public static final double PRIORITY = Priority.NORMAL_PRIORITY;

	private WeakReference<DataRow> m_row = new WeakReference<>(null);
	private WeakReference<DataTableSpec> m_spec = new WeakReference<>(null);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataRow getInputDataRow() {
		return m_row.get();
	}

	/**
	 * Set the contained DataRow;
	 *
	 * @param dataRow
	 */
	@Override
	public void setInputDataRow(final DataRow dataRow) {
		m_row = new WeakReference<>(dataRow);
	}

	@Override
	public DataTableSpec getInputDataTableSpec() {
		return m_spec.get();
	}

	@Override
	public void setDataTableSpec(final DataTableSpec spec) {
		m_spec = new WeakReference<>(spec);
	}

}
