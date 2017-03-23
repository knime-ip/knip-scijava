package org.knime.scijava.commands.exp.testdriven.table2table.v2.nodes;

import org.knime.scijava.commands.exp.testdriven.table2table.v2.Interfaces.Filter;
import org.knime.scijava.commands.exp.testdriven.table2table.v2.Interfaces.TableProxy;
import org.scijava.plugin.Parameter;

public class NAryFilter implements Filter<NAryFilter.Input> {

    public static class Input implements TableProxy {
        @Parameter
        int iter;
    }

    @Override
    public boolean eval(final Input input) {
        return input.iter < 5;
    }
}
