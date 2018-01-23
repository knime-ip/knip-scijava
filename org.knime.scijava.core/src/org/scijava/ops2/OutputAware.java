package org.scijava.ops2;

@FunctionalInterface
public interface OutputAware<I, O> {
	O createOutput(I in);
}
