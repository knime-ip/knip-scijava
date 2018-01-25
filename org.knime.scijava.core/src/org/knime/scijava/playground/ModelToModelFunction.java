package org.knime.scijava.playground;

import java.util.function.Function;

import org.scijava.param2.Parameter;
import org.scijava.struct2.ItemIO;

// TODO: hack to disable column selection generation
@Parameter(key = "input", type = ItemIO.OUTPUT, struct = true, persist = false)
@Parameter(key = "output", type = ItemIO.OUTPUT, struct = true, persist = false)
public interface ModelToModelFunction<I, O> extends Function<I, O> {

	Object configure(Object in);
}
