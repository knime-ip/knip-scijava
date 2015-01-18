package org.knime.knip.scijava.commands.impl;

import org.knime.core.data.DataRow;
import org.knime.knip.scijava.commands.InputDataRowService;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

/**
 * Default implementation of InputDataRowService.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputDataRowService.class, priority = DefaultInputDataRowService.PRIORITY)
public class DefaultInputDataRowService extends AbstractService implements
		InputDataRowService {
	
	/**
	 * Priority of this {@link Plugin}
	 */
	public static final double PRIORITY = Priority.NORMAL_PRIORITY;

	private DataRow m_row = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataRow getInputDataRow() {
		return m_row;
	}

	/**
	 * Set the contained DataRow;
	 * @param dataRow
	 */
	public void setDataRow(DataRow dataRow) {
		m_row = dataRow;
	}
	
}
