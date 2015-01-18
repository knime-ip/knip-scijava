package org.knime.knip.scijava.commands.impl;

import org.knime.core.data.DataCell;
import org.knime.knip.scijava.commands.InputDataRowService;
import org.knime.knip.scijava.commands.KnimePreprocessor;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.knime.knip.scijava.commands.adapter.InputAdapterService;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.module.process.AbstractPreprocessorPlugin;
import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.widget.InputHarvester;

/**
 * A preprocessor plugin that handles unresolved parameters of any type by
 * filling them from a KNIME DataRow.
 * 
 * Requires {@link InputAdapterService} and {@link InputDataRowService}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = PreprocessorPlugin.class, priority = DefaultKnimePreprocessor.PRIORITY)
public class DefaultKnimePreprocessor extends AbstractPreprocessorPlugin
		implements KnimePreprocessor {

	public static final double PRIORITY = InputHarvester.PRIORITY + 1.0;

	@Parameter
	private InputAdapterService inputAdapters;

	@Parameter
	private InputDataRowService dataRowIn;

	/**
	 * Straight forward implementation of filling the input of a module from a
	 * DataRow. For every input searches linearly for a DataCell in the DataRow
	 * which can be adapted to the inputs type.
	 * 
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void process(Module module) {
		for (ModuleItem<?> i : module.getInfo().inputs()) {
			if (module.isResolved(i.getName())) {
				continue;
			}
			for (DataCell c : dataRowIn.getInputDataRow()) {
				InputAdapter inputAdapter = inputAdapters
						.getMatchingInputAdapter(c.getType().getCellClass(),
								i.getType());

				if (inputAdapter != null) {
					module.setInput(i.getName(), inputAdapter.getValue(c));
					module.setResolved(i.getName(), true);

					// found a matching cell, continue with next input.
					break;
				}
			}
		}
	}

}
