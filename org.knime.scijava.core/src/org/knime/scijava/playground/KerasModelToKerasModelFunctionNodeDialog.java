package org.knime.scijava.playground;

import org.scijava.param2.ValidityException;

public class KerasModelToKerasModelFunctionNodeDialog extends ModelToModelFunctionNodeDialog {

	public KerasModelToKerasModelFunctionNodeDialog(final KerasModelToKerasModelFunction func)
			throws ValidityException {
		super(func);
	}
}
