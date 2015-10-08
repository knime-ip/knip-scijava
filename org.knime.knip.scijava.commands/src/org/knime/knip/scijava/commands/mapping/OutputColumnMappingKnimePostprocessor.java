package org.knime.knip.scijava.commands.mapping;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DefaultRow;
import org.knime.knip.scijava.commands.InputDataRowService;
import org.knime.knip.scijava.commands.KnimePostprocessor;
import org.knime.knip.scijava.commands.OutputDataRowService;
import org.knime.knip.scijava.commands.adapter.OutputAdapter;
import org.knime.knip.scijava.commands.adapter.OutputAdapterService;
import org.scijava.Priority;
import org.scijava.log.LogService;
import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.module.process.AbstractPostprocessorPlugin;
import org.scijava.module.process.PostprocessorPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Preprocessor which fills unresolved {@link Module} inputs from KNIME
 * {@link DataTable}s using {@link ColumnModuleItemMapping}s from a
 * {@link OutputToColumnMappingService}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = PostprocessorPlugin.class, priority = Priority.NORMAL_PRIORITY
		+ 1)
public class OutputColumnMappingKnimePostprocessor
		extends AbstractPostprocessorPlugin implements KnimePostprocessor {

	@Parameter
	private OutputAdapterService adapterService;

	@Parameter
	private InputDataRowService dataRowIn;

	@Parameter
	private OutputDataRowService dataRowOut;

	@Parameter
	private OutputToColumnMappingService outputMappingService;

	@Parameter
	private LogService log;

	/**
	 * Straight forward implementation of module output to DataRow: For every
	 * output which has a mapping find an OutputAdapter to create a DataCell
	 * from it and create a DefaultDataRow from created DataCells.
	 * 
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void process(Module module) {

		List<DataCell> cells = new ArrayList<DataCell>();

		for (ModuleItem i : module.getInfo().outputs()) {
			if (outputMappingService.getMappingForModuleItem(i) == null) {
				// No mapping for this output. Will not create a cell.
				continue;
			}

			final OutputAdapter<?, DataCell> outputAdapter = adapterService
					.getMatchingOutputAdapter(i.getType());

			if (outputAdapter != null) {
				cells.add(outputAdapter.convert(module.getOutput(i.getName()),
						DataCell.class));
			} else {
				log.warn("Could not find an OutputAdapter for \"" + i.getName()
						+ "\".");
			}
		}

		dataRowOut.setOutputDataRow(
				new DefaultRow(dataRowIn.getInputDataRow().getKey(), cells));
	}
}
