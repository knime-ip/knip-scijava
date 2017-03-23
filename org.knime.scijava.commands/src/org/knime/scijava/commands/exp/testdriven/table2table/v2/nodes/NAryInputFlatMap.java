package org.knime.scijava.commands.exp.testdriven.table2table.v2.nodes;

import java.util.function.Consumer;

import org.knime.scijava.commands.exp.testdriven.table2table.v2.Interfaces;
import org.knime.scijava.commands.exp.testdriven.table2table.v2.Interfaces.FlatMap;
import org.knime.scijava.commands.exp.testdriven.table2table.v2.Interfaces.TableProxy;
import org.scijava.plugin.Parameter;

public class NAryInputFlatMap
        implements FlatMap<NAryInputFlatMap.Input, Integer> {

    public static class Input implements TableProxy {
        @Parameter
        int iter;
    }

    @Override
    public void flatMap(final Input input, final Consumer<Integer> output) {
        for (int i = 0; i < input.iter; i++) {
            output.accept(i);
        }
    }
}
