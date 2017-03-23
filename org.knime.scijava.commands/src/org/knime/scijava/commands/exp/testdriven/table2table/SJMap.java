package org.knime.scijava.commands.exp.testdriven.table2table;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

// ONE SHOULD BE ABLE TO WRITE COMMANDS/Nodes LIKE THIS!
public interface SJMap<IN, OUT>
        extends BiConsumer<IN, Consumer<OUT>>, SJTableToTable<IN, OUT> {
    // NB
}
