package org.knime.scijava.commands.exp.testdriven.table2table;

import org.scijava.plugin.SciJavaPlugin;

public interface SJTableToTable<IN, OUT> extends SciJavaPlugin {

    Class<? extends IN> inType();

    Class<? extends OUT> outType();
}
