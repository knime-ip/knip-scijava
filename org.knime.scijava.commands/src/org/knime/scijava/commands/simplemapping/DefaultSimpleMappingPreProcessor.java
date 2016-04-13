package org.knime.scijava.commands.simplemapping;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.scijava.commands.adapter.InputAdapter;
import org.knime.scijava.commands.adapter.InputAdapterService;
import org.knime.scijava.commands.io.InputDataRowService;
import org.knime.scijava.commands.process.KnimePreprocessor;
import org.scijava.Priority;
import org.scijava.log.LogService;
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
	SimpleColumnMappingService m_colMap;
	@Parameter
	InputDataRowService m_inrow;
	@Parameter
	InputAdapterService m_ias;
	@Parameter
	LogService m_log;

	@Override
	public void process(final Module module) {
		final DataTableSpec spec = m_inrow.getInputDataTableSpec();
		final DataRow row = m_inrow.getInputDataRow();

		for (final String inputName : m_colMap.getMappedInputs()) {

			final String mappedColumn = m_colMap.getMappedColumn(inputName);
			final ModuleItem<?> input = module.getInfo().getInput(inputName);

			if (mappedColumn == null) { // Error or optional column
				if (input.isRequired()) {
					cancel("Couldn't find mapping for input \"" + inputName
							+ "\"! Mapping is invalid.");
				} else {
					// Input is optional and can be null
					module.setInput(inputName, null);
					module.setResolved(inputName, true);
					return;
				}
			}

			DataCell cell = null;

			try {
				cell = row.getCell(spec.findColumnIndex(mappedColumn));
			} catch (final IndexOutOfBoundsException e) {
				// getColumnIndex() might return -1 or a index greater the
				// column count
				String errortext = "Couldn't find column \"" + mappedColumn
						+ "\" which is mapped to input " + inputName + ".";
				m_log.error(errortext);
				cancel(errortext);
			}

			// find a input adapter which can convert the cells value to the
			// a type required by the input
			@SuppressWarnings("unchecked")
			final InputAdapter<DataCell, ?> ia = m_ias
					.getMatchingInputAdapter(cell.getClass(), input.getType());

			if (ia == null) {
				cancel("No InputAdapter for: " + cell.getClass() + " > "
						+ input.getType().getCanonicalName());
				return;
			}

			// set the input and mark resolved
			Object converted = null;
			try {
				converted = ia.convert(cell, input.getType());
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(
						"Could not process value for input: " + inputName
								+ ", the mapped column: \"" + mappedColumn
								+ "\" contains an illegal value: "
								+ e.getMessage());
			}
			module.setInput(inputName, converted);
			module.setResolved(inputName, true);
		}
	}

}
