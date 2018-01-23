
package org.scijava.ops2;

import java.util.function.Consumer;

/** {@link Consumer}, as a plugin. */
@FunctionalInterface
public interface SinkOp<I> extends Consumer<I> {}
