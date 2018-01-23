package org.knime.scijava.base;

import org.knime.scijava.base.node.StructFunction;
import org.scijava.param2.Parameter;

public class MyFunction implements StructFunction<InStruct, OutStruct> {

	@Parameter(label = "Select the best prefix ever")
	private String prefix;

	@Parameter(label = "Select a multiplier")
	private int multiplier;

	@Override
	public OutStruct apply(InStruct t) {
		int paramOne = t.paramOne;
		String paramTwo = t.paramTwo;

		// FIXME: avoid object creation here if possible. However, must be
		// supported by API
		return new OutStruct("Integer Multiplied: " + (paramOne * multiplier), prefix + " " + paramTwo);
	}
}
