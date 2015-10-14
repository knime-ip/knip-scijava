package org.knime.knip.scijava.commands;

import java.lang.ref.WeakReference;

import org.knime.core.node.ExecutionContext;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

/**
 * Default implementation of KnimeExecutionService. Holds a KNIME Node
 * {@link ExecutionContext} in a {@link WeakReference}, which ensures that the
 * {@link ExecutionContext} can be garbage collected when execution of a Node
 * terminates.
 *
 * @author Jonathan Hale (University of Konstanz)
 * @see ExecutionContext
 */
@Plugin(type = KNIMEExecutionService.class, priority = DefaultKnimeExecutionService.PRIORITY)
public class DefaultKnimeExecutionService extends AbstractService
		implements KNIMEExecutionService {

	/**
	 * Priority of this {@link Plugin}
	 */
	public static final double PRIORITY = Priority.NORMAL_PRIORITY;

	private WeakReference<ExecutionContext> m_exec = new WeakReference<>(null);

	/**
	 * Set the {@link ExecutionContext}. Note that this service holds the
	 * {@link ExecutionContext} in a {@link WeakReference}, which means that the
	 * reference needs to be kept valid outside the service.
	 *
	 * @param context
	 */
	@Override
	public void setExecutionContext(final ExecutionContext context) {
		m_exec = new WeakReference<>(context);
	}

	@Override
	public ExecutionContext getExecutionContext() {
		return m_exec.get();
	}

}
