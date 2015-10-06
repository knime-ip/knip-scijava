package org.knime.knip.scijava.commands;

import org.knime.core.data.DataRow;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

/**
 * Default implementation of OutputDataRowService.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = OutputDataRowService.class, priority = DefaultOutputDataRowService.PRIORITY)
public class DefaultOutputDataRowService extends AbstractService implements
		OutputDataRowService {

	/**
	 * Priority of this {@link Plugin}
	 */
	public static final double PRIORITY = Priority.NORMAL_PRIORITY;

	private DataRow m_row = null;

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void setOutputDataRow(DataRow r) {
		m_row = r;
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public DataRow getOutputDataRow() {
		return m_row;
	}

}
