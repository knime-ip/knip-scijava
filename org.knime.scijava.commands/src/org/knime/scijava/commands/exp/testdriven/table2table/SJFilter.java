package org.knime.scijava.commands.exp.testdriven.table2table;

import java.util.function.Function;
import java.util.function.Predicate;

import org.scijava.plugin.Parameter;

// ONE SHOULD BE ABLE TO WRITE COMMANDS/Nodes LIKE THIS!
public class SJFilter<IN> implements Function<IN, Boolean> {

    @Parameter
    private Predicate<IN> predicate;

    @Override
    public Boolean apply(final IN t) {
        return predicate.test(t);
    }

}
