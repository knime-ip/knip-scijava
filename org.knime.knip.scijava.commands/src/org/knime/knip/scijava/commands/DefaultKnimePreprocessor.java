package org.knime.knip.scijava.commands;

import org.knime.core.data.DataValue;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.knime.knip.scijava.commands.adapter.InputAdapterService;
import org.knime.knip.scijava.commands.mapping.ColumnInputMappingKnimePreprocessor;
import org.knime.knip.scijava.commands.settings.DefaultNodeSettingsHarvester;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.module.process.AbstractPreprocessorPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.widget.InputHarvester;

/**
 * A preprocessor plugin that handles unresolved parameters of any type by
 * filling them from a KNIME DataRow.
 *
 * Requires {@link InputAdapterService} and {@link InputDataRowService}.
 *
 * @author Jonathan Hale (University of Konstanz)
 * @deprecated Use something not so random, like
 *             {@link ColumnInputMappingKnimePreprocessor} or
 *             {@link DefaultNodeSettingsHarvester}.
 */
@Deprecated
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
	@SuppressWarnings({ "unchecked" })
	@Override
	public void process(final Module module) {
		for (final ModuleItem<?> i : module.getInfo().inputs()) {
			// this is the case when we resolved the module via e.g. UI
			if (module.isResolved(i.getName())) {
				continue;
			}

			// if not, we have to resolve the value from the incoming DataRow.
			// However, the corresponding row should _always_ be controlled.
			for (final DataValue c : dataRowIn.getInputDataRow()) {
				final InputAdapter<DataValue, Object> inputAdapter = inputAdapters
						.getMatchingInputAdapter(c.getClass(), i.getType());

				if (inputAdapter != null) {
					module.setInput(i.getName(),
							inputAdapter.convert(c, i.getType()));
					module.setResolved(i.getName(), true);

					// found a matching cell, continue with next input.
					break;
				}
			}
		}
	}

}
