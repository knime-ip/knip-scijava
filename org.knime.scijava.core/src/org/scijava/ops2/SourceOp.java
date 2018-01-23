
package org.scijava.ops2;

import java.util.function.Supplier;

/** {@link Supplier}, as a plugin. */
@FunctionalInterface
public interface SourceOp<O> extends Supplier<O> {}
