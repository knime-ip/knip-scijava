package org.knime.scijava.commands.exp.testdriven.table2table.v2.nodes;

import java.util.function.Consumer;

import org.knime.scijava.commands.exp.testdriven.table2table.v2.Interfaces;
import org.knime.scijava.commands.exp.testdriven.table2table.v2.Interfaces.FlatMap;
import org.knime.scijava.commands.exp.testdriven.table2table.v2.Interfaces.TableProxy;
import org.scijava.plugin.Parameter;

public class NAryOutputFlatMap
        implements FlatMap<Integer, NAryOutputFlatMap.Output> {

    @Parameter
    private double sigma;

    @Override
    public void flatMap(final Integer input,
            final Consumer<NAryOutputFlatMap.Output> output) {
        final Output out = new Output();
        out.someInt = 5;
        out.asString = "Look" + out.someInt;
        output.accept(out);
    }

    // Proxies

    public static class Output implements TableProxy {
        @Parameter
        private int someInt;

        @Parameter
        private String asString;
    }
}
