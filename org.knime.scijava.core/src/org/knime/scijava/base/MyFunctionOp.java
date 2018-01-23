package org.knime.scijava.base;

import org.scijava.ops.FunctionOp;
import org.scijava.param.Parameter;

public class MyFunctionOp implements FunctionOp<InStruct, OutStruct> {

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
