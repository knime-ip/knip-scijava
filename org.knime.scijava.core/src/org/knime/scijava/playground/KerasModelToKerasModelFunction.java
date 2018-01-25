package org.knime.scijava.playground;

import org.scijava.param2.Parameter;
import org.scijava.struct2.ItemIO;

public abstract class KerasModelToKerasModelFunction implements
		ModelToModelFunction<KerasModelToKerasModelFunction.KerasModelStruct, KerasModelToKerasModelFunction.KerasModelStruct> {

	public static class KerasModelStruct {

		// TODO: hack to disable column selection generation
		@Parameter(label = "Deep learning Keras network", type = ItemIO.OUTPUT)
		Object m_model;
	}
}
