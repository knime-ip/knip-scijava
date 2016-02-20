package org.knime.scijava.commands.simplemapping;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.scijava.commands.adapter.InputAdapter;
import org.knime.scijava.commands.adapter.InputAdapterService;
import org.knime.scijava.commands.io.InputDataRowService;
import org.knime.scijava.commands.process.KnimePreprocessor;
import org.scijava.Priority;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.module.process.AbstractPreprocessorPlugin;
import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = PreprocessorPlugin.class, priority = Priority.HIGH_PRIORITY)
public class DefaultSimpleMappingPreProcessor extends AbstractPreprocessorPlugin
		implements KnimePreprocessor {

	@Parameter
	SimpleColumnMappingService colMap;
	@Parameter
	InputDataRowService inrow;
	@Parameter
	InputAdapterService ias;

	@Override
	public void process(final Module module) {
		final DataTableSpec spec = inrow.getInputDataTableSpec();
		final DataRow row = inrow.getInputDataRow();

		for (final String inputName : colMap.getMappedInputs()) {

			final String mappedColumn = colMap.getMappedColumn(inputName);
			final ModuleItem<?> input = module.getInfo().getInput(inputName);

			DataCell cell = null;

			try {
				cell = row.getCell(spec.findColumnIndex(mappedColumn));
			} catch (final IndexOutOfBoundsException e) {
				// getColumnIndex() might return -1 or a index greater the
				// column count
				cancel("Couldn't find column \"" + mappedColumn
						+ "\" which is mapped to input " + inputName + ".");
			}

			// find a input adapter which can convert the cells value to the
			// a type required by the input
			@SuppressWarnings("unchecked")
			final InputAdapter<DataCell, ?> ia = ias
					.getMatchingInputAdapter(cell.getClass(), input.getType());

			if (ia == null) {
				cancel("No InputAdapter for: " + cell.getClass() + " > "
						+ input.getType().getCanonicalName());
				return;
			}

			// set the input and mark resolved
			module.setInput(inputName, ia.convert(cell, input.getType()));
			module.setResolved(inputName, true);
		}
	}

}
