package org.knime.scijava.base;

import org.knime.scijava.base.node.KnimeFunction;
import org.scijava.param2.Parameter;

public class MultiplicationFunction
		implements KnimeFunction<MultiplicationFunction.InStruct, MultiplicationFunction.OutStruct> {

	@Parameter(label = "Multiplier", min = "0", max = "200", stepSize = "1")
	int Multiplier;

	@Override
	public OutStruct apply(final InStruct t) {
		return new OutStruct(t.m_value * Multiplier);
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
