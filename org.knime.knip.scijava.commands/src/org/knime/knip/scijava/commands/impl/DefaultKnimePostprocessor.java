package org.knime.knip.scijava.commands.impl;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.def.DefaultRow;
import org.knime.knip.scijava.commands.InputDataRowService;
import org.knime.knip.scijava.commands.KnimePostprocessor;
import org.knime.knip.scijava.commands.OutputDataRowService;
import org.knime.knip.scijava.commands.adapter.OutputAdapter;
import org.knime.knip.scijava.commands.adapter.OutputAdapterService;
import org.scijava.Priority;
import org.scijava.log.LogService;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.module.process.AbstractPostprocessorPlugin;
import org.scijava.module.process.PostprocessorPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of KnimePostprocessor.
 * 
 * Requires {@link OutputAdapterService}, {@link InputDataRowService} and
 * {@link OutputDataRowService}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = PostprocessorPlugin.class, priority = DefaultKnimePostprocessor.PRIORITY)
public class DefaultKnimePostprocessor extends AbstractPostprocessorPlugin
		implements KnimePostprocessor {

	public static final double PRIORITY = Priority.NORMAL_PRIORITY;

	@Parameter
	private OutputAdapterService adapterService;

	@Parameter
	private InputDataRowService dataRowIn;

	@Parameter
	private OutputDataRowService dataRowOut;
	
	@Parameter
	private LogService log;

	/**
	 * Straight forward implementation of module output to DataRow: For every
	 * output find an OutputAdapter to create a DataCell from it and create a
	 * DefaultDataRow from created DataCells.
	 * 
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void process(Module module) {

		List<DataCell> dataCells = new ArrayList<DataCell>();

		for (ModuleItem i : module.getInfo().outputs()) {
			OutputAdapter outputAdapter = adapterService
					.getMatchingOutputAdapter(i.getType());

			if (outputAdapter != null) {
				dataCells.add(outputAdapter.createCell(module.getOutput(i
						.getName())));
			} else {
				log.warn("Could not find a OutputAdapter for \"" + i.getName() + "\".");
			}
		}

		DataRow dataRow = new DefaultRow(dataRowIn.getInputDataRow().getKey(),
				dataCells);
		dataRowOut.setOutputDataRow(dataRow);
	}

}
