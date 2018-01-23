package org.scijava.ops2;

import java.util.function.BiConsumer;

/** Unary computer. */
@FunctionalInterface
public interface ComputerOp<I, O> extends BiConsumer<I, O>
{
}