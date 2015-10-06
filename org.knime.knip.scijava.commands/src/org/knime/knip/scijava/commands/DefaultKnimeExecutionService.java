package org.knime.knip.scijava.commands;

import org.knime.core.node.ExecutionContext;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

/**
 * Default implementation of KnimeExecutionService.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = KnimeExecutionService.class, priority = DefaultKnimeExecutionService.PRIORITY)
public class DefaultKnimeExecutionService extends AbstractService implements
		KnimeExecutionService {

	/**
	 * Priority of this {@link Plugin}
	 */
	public static final double PRIORITY = Priority.NORMAL_PRIORITY;

	private ExecutionContext m_exec = null;

	public void setExecutionContex(ExecutionContext e) {
		m_exec = e;
	}
	
	@Override
	public ExecutionContext getExecutionContext() {
		return m_exec;
	}

}
