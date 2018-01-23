package org.knime.scijava.base;

import org.scijava.param2.Parameter;

public class InStruct {

	@Parameter(label = "Some Number", min = "0", max = "1000", stepSize = "10")
	int paramOne;

	@Parameter(label = "Input String")
	String paramTwo;
}
