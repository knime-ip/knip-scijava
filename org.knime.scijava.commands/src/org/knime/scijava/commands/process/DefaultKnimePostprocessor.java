package org.knime.scijava.commands.process;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.scijava.commands.converter.ConverterCacheService;
import org.knime.scijava.commands.io.OutputDataRowService;
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
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@Plugin(type = PostprocessorPlugin.class, priority = Priority.NORMAL_PRIORITY)
public class DefaultKnimePostprocessor extends AbstractPostprocessorPlugin
        implements KnimePostprocessor {

    @Parameter
    private ConverterCacheService m_converters;

    @Parameter
    private OutputDataRowService m_dataRowOut;

    @Parameter
    private LogService m_log;

    /**
     * Straight forward implementation of module output to DataRow: For every
     * output find an OutputAdapter to create a DataCell from it and create a
     * DefaultDataRow from created DataCells.
     *
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes" })
    @Override
    public void process(final Module module) {

        final List<DataCell> cells = new ArrayList<>(
                module.getOutputs().size());

        for (final ModuleItem i : module.getInfo().outputs()) {

            // FIXME Ugly hack!
            if (i.getName().equals("result")) {
                continue;
            }

            DataCell out = null;
            try {
                out = m_converters.convertToKnime(module.getOutput(i.getName()),
                        i.getType());
            } catch (final Exception e) {
                throw new IllegalArgumentException(e);
            }
            cells.add(out);
        }

        m_dataRowOut.setOutputCells(cells);
    }
}
