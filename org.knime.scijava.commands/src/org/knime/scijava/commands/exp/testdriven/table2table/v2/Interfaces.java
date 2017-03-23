package org.knime.scijava.commands.exp.testdriven.table2table.v2;

import java.io.Serializable;
import java.util.function.Consumer;

import org.scijava.plugin.SciJavaPlugin;

public class Interfaces {

    public interface FlatMap<I, O> {
        void flatMap(final I input, final Consumer<O> output);
    }

    public interface Map<I, O> {
        O map(final I input);
    }

    public interface Filter<I extends TableProxy> {
        boolean eval(final I input);
    }

    public interface TableProxy extends SciJavaPlugin, Serializable {
        // NB: Marker interface, however, we can extend it later with some
        // methods we expect.

        // NB: Serializable required for external execution.
    }

}
