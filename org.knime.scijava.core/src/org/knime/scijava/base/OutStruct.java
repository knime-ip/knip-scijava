package org.knime.scijava.base;

import org.scijava.param2.Parameter;

public class OutStruct {

	@Parameter(label = "Some String")
	private String param1;

	@Parameter(label = "Another String")
	private String param2;

	public OutStruct(String param1, String param2) {
		this.param1 = param1;
		this.param2 = param2;
	}
}
