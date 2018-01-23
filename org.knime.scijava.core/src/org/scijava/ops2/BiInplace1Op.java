package org.scijava.ops2;

import java.util.function.BiConsumer;

/** Binary inplace computation mutating the first input. */
@FunctionalInterface
public interface BiInplace1Op<IO, I2> extends BiConsumer<IO, I2> {}