package org.knime.scijava.commands.mapping;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
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

/**
 * Preprocessor which fills unresolved {@link Module} inputs from KNIME
 * {@link DataTable}s using a {@link ColumnToInputMappingService}.
 *
 *
 * @author Jonathan Hale (University of Konstanz)
 */
// Low Priority ensures that this is run after the other preprocessors
@Plugin(type = PreprocessorPlugin.class, priority = Priority.LOW_PRIORITY)
public class ColumnInputMappingKnimePreprocessor
		extends AbstractPreprocessorPlugin implements KnimePreprocessor {

	@Parameter
	private ColumnToInputMappingService m_cimService;

	@Parameter
	private InputDataRowService m_inputTable;

	@Parameter
	private InputAdapterService m_inputAdapters;

	@Parameter
	private LogService m_log;

	@Override
	public void process(final Module module) {
		// get the DataTableSpec to later find column indices
		final DataTableSpec spec = m_inputTable.getInputDataTableSpec();

		// some local variables set and used in the following loop
		String inputName;

		// DataRow will remain the same while processing, this is a shortcut to
		// it.
		final DataRow row = m_inputTable.getInputDataRow();

		// try to set module input values from the current DataRow
		for (final ModuleItem<?> i : module.getInfo().inputs()) {
			inputName = i.getName();

			// the input may have already been filled by a previous
			// preprocessor, in that case there is nothing left to do for us.
			if (!module.isResolved(inputName)) {
				// get a column to input mapping

				if (!m_cimService.isItemMapped(inputName)) {
					cancel("Couldn't find an active column input mapping for input \""
							+ inputName + "\".");
				}

				// try to get the data cell matching the mapped column
				DataCell cell = null;
				final String mappedColumn = m_cimService
						.getColumnNameForInput(inputName);
				if (mappedColumn == null) {
					return; // there is no mapping for this column
				}
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
				final InputAdapter<DataCell, ?> ia = m_inputAdapters
						.getMatchingInputAdapter(cell.getClass(), i.getType());

				if (ia == null) {
					cancel("No InputAdapter for: " + cell.getClass() + " > "
							+ i.getType().getCanonicalName());
					return;
				}

				// set the input and mark resolved
				module.setInput(inputName, ia.convert(cell, i.getType()));
				module.setResolved(inputName, true);
			}
		}
	}
}
