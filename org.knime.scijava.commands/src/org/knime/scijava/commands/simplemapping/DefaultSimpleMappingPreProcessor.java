package org.knime.scijava.commands.simplemapping;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.scijava.commands.converter.ConverterCacheService;
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
    private SimpleColumnMappingService m_colMap;
    @Parameter
    private InputDataRowService m_inrow;
    @Parameter
    private LogService m_log;
    @Parameter
    private ConverterCacheService m_converters;

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
                final String errortext = "Couldn't find column \"" + mappedColumn
                        + "\" which is mapped to input " + inputName + ".";
                m_log.error(errortext, e);
                cancel(errortext);
            }

            // set the input and mark resolved
            Object converted;
            try {
                converted = m_converters.convertToJava(cell, input.getType());
            } catch (final Exception e) {
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
