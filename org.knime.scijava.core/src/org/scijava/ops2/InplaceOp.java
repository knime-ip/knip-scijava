
package org.scijava.ops2;

import java.util.function.Consumer;

/** Unary inplace computation. */
@FunctionalInterface
public interface InplaceOp<IO> extends Consumer<IO> {}
