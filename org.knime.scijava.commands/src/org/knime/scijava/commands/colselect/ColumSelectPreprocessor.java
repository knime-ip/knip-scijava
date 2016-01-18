package org.knime.scijava.commands.colselect;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.scijava.commands.adapter.InputAdapterService;
import org.knime.scijava.commands.io.InputDataRowService;
import org.knime.scijava.commands.process.KnimePreprocessor;
import org.scijava.Priority;
import org.scijava.module.Module;
import org.scijava.module.process.AbstractPreprocessorPlugin;
import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = PreprocessorPlugin.class, priority = Priority.HIGH_PRIORITY)
public class ColumSelectPreprocessor extends AbstractPreprocessorPlugin
		implements KnimePreprocessor {

	@Parameter
	private InputDataRowService m_inputRow;

	@Parameter
	private InputAdapterService m_inputAdapters;

//	@Parameter
//	private


	@Override
	public void process(Module module) {

		final DataTableSpec spec = m_inputRow.getInputDataTableSpec();

		DataRow row = m_inputRow.getInputDataRow();




	}

}
