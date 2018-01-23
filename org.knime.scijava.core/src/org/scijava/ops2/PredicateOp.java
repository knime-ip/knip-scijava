
package org.scijava.ops2;

import java.util.function.Predicate;

/** {@link Predicate}, as a plugin. */
@FunctionalInterface
public interface PredicateOp<IO> extends Predicate<IO> {}
