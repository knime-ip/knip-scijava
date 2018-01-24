package org.knime.scijava.base;

import org.knime.scijava.base.node.StructFunction;
import org.scijava.param2.Parameter;

public class MultiplicationFunction
		implements StructFunction<MultiplicationFunction.InStruct, MultiplicationFunction.OutStruct> {

	@Parameter(label = "Multiplier", min = "0", max = "2147483647", stepSize = "1")
	int multiplier;

	@Override
	public OutStruct apply(final InStruct t) {
		// FIXME: Avoid object creation here if possible. However, must be
		// supported by API.
		return new OutStruct(t.m_value * multiplier);
	}

	public static class InStruct {

		@Parameter(label = "Some Number")
		double m_value;
	}

	public static class OutStruct {

		@Parameter(label = "Result")
		double m_value;

		public OutStruct(final double value) {
			m_value = value;
		}
	}
}
