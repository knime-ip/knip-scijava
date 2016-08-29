package org.knime.scijava.commands.module;

import org.knime.core.data.DataRow;
import org.knime.core.node.ExecutionContext;
import org.knime.scijava.commands.CellOutput;

public interface NodeModule {

    void run(final DataRow input, final CellOutput output,
            final ExecutionContext ctx) throws Exception;
}
