package org.knime.scijava.commands.exp.testdriven.table2table;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import org.scijava.plugin.SciJavaPlugin;

// ONE SHOULD BE ABLE TO WRITE COMMANDS/Nodes LIKE THIS!
public class SJAggregate<IN, OUT> implements SciJavaPlugin {

    public void reduce(final OUT memo, final BiFunction<OUT, ? super IN, OUT> f,
            final BinaryOperator<OUT> merge) {

    }

    public OUT createMemo() {
        return null;
    }

}
