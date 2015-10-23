package org.knime.scijava.commands;

import org.knime.core.node.ExecutionContext;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

/**
 * Service holding an {@link ExecutionContext}
 *
 * <p>
 * KnimeExecutionService plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link KNIMEExecutionService}.class.
 * </p>
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 */
public interface KNIMEExecutionService extends Service {

	/**
	 * @return the ExecutionContext held by this service, or null if there is
	 *         none.
	 */
	ExecutionContext getExecutionContext();

	/**
	 * Set the ExecutionContext for this service to hold.
	 */
	void setExecutionContext(ExecutionContext context);

}
