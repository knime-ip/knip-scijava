package org.knime.scijava.playground;

import org.scijava.param2.Parameter;

public class KerasDenseLayerFunction extends KerasModelToKerasModelFunction {

	@Parameter(label = "Units", min = "0", max = "200", stepSize = "1")
	int units = 10;

	@Parameter(label = "Activation")
	String activation = "relu";

	@Override
	public Object configure(final Object in) {
		// TODO: dummy
		return in;
	}

	@Override
	public KerasModelStruct apply(final KerasModelStruct t) {
		// TODO: dummy
		return t;
	}
}
