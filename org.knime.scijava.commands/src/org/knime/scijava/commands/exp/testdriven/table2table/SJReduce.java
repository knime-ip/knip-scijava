package org.knime.scijava.commands.exp.testdriven.table2table;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.SciJavaPlugin;

// ONE SHOULD BE ABLE TO WRITE COMMANDS/Nodes LIKE THIS!
public class SJReduce<IN, OUT>
        implements BiConsumer<Iterable<IN>, Consumer<OUT>>, SciJavaPlugin {

    @Parameter
    private String input;

    @Override
    public void accept(final Iterable<IN> t, final Consumer<OUT> u) {
        //
    }
}
