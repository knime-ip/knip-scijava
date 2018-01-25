package org.knime.scijava.playground;

import org.knime.scijava.base.node.KnimeFunction;
import org.scijava.param2.Parameter;

public class GaussianFilterFunction
		implements KnimeFunction<GaussianFilterFunction.InStruct, GaussianFilterFunction.OutStruct> {

	@Parameter(label = "Multiplier", min = "0", max = "200", stepSize = "1")
	int sigma = 1;

	@Override
	public OutStruct apply(final InStruct t) {
		throw new RuntimeException("not yet implemented"); // TODO: NYI
	}

	public static class InStruct {

		@Parameter(label = "Some Image")
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
