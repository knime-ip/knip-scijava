package org.knime.knip.scijava.commands;

import org.knime.core.node.ExecutionContext;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

/**
 * Service holding an {@link ExecutionContext}
 * 
 * <p>
 * KnimeExecutionService plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link KnimeExecutionService}.class.
 * </p>
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
public interface KnimeExecutionService extends Service {

	ExecutionContext getExecutionContext();

}
