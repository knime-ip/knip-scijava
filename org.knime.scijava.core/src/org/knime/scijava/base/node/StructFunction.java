package org.knime.scijava.base.node;

import java.util.function.Function;

import org.scijava.param2.Parameter;
import org.scijava.struct2.ItemIO;

@Parameter(key = "input", struct = true, persist = false)
@Parameter(key = "output", type = ItemIO.OUTPUT, struct = true, persist = false)
public interface StructFunction<I, O> extends Function<I, O> {
}
