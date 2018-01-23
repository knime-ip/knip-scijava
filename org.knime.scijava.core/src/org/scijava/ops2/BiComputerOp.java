package org.scijava.ops2;

/** Binary computer. */
@FunctionalInterface
public interface BiComputerOp<I1, I2, O> extends TriConsumer<I1, I2, O>
{
}