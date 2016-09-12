package org.knime.scijava.commands.module;

import org.knime.core.data.DataRow;
import org.knime.core.node.ExecutionContext;
import org.knime.scijava.commands.CellOutput;

/**
 * Interface for NodeModules, wrappers around ModuleInfo which can be executed
 * by KNIME.
 *
 * @author Christian Dietz, University of Konstanz
 */
public interface NodeModule {

    /**
     * Run the module on a {@link DataRow}.
     *
     * @param input
     *            the {@link DataRow}.
     * @param output
     *            Where to push output to.
     * @param ctx
     *            KNIME {@link ExecutionContext}.
     * @throws Exception
     *             If an exception occurs while executing the module.
     */
    void run(final DataRow input, final CellOutput output,
            final ExecutionContext ctx) throws Exception;
}
