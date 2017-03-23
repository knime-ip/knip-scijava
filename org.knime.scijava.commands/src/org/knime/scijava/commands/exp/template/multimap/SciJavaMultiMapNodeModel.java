package org.knime.scijava.commands.exp.template.multimap;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.streamable.BufferedDataTableRowOutput;
import org.knime.core.node.streamable.DataTableRowInput;
import org.knime.core.node.streamable.InputPortRole;
import org.knime.core.node.streamable.OutputPortRole;
import org.knime.core.node.streamable.PartitionInfo;
import org.knime.core.node.streamable.PortInput;
import org.knime.core.node.streamable.PortOutput;
import org.knime.core.node.streamable.RowInput;
import org.knime.core.node.streamable.RowOutput;
import org.knime.core.node.streamable.StreamableOperator;
import org.knime.scijava.commands.exp.testdriven.table2table.SJDataRowToCells;
import org.knime.scijava.commands.exp.testdriven.table2table.SJMap;
import org.knime.scijava.commands.module.NodeModuleService;
import org.scijava.Context;
import org.scijava.convert.ConvertService;
import org.scijava.plugin.Parameter;

/**
 * NodeModel of ScijavaCommandNodes.
 *
 * @author Christian Dietz, University of Konstanz
 */
public class SciJavaMultiMapNodeModel<IN, OUT> extends NodeModel {

    @Parameter
    private NodeModuleService nodeService;

    @Parameter
    private ConvertService cs;

    protected SciJavaMultiMapNodeModel(final Context ctx,
            final SJMap<IN, OUT> map) {
        super(1, 1);
        ctx.inject(this);
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        return new DataTableSpec[] {
                nodeService.createOutSpec(mapper, inSpecs[0]) };
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

        final DataTableSpec inSpec = inData[0].getSpec();

        final BufferedDataTableRowOutput rowOutput = new BufferedDataTableRowOutput(
                exec.createDataContainer(
                        nodeService.createOutSpec(mapper, inSpec)));

        run(new DataTableRowInput(inData[0]), rowOutput, exec,
                inData[0].size());

        rowOutput.close();

        return new BufferedDataTable[] { rowOutput.getDataTable() };
    }

    private void run(final RowInput rowInput, final RowOutput rowOutput,
            final ExecutionContext exec, final long totalRows) {
        try {

            final SJDataRowToCells<IN, OUT> mapper = null;

            // TODO make own class?
            final Consumer<DataCell[]> consumer = new Consumer<DataCell[]>() {

                long i = 0;

                @Override
                public void accept(final DataCell[] t) {
                    // TODO proper key handling etc...
                    try {
                        rowOutput.push(
                                new DefaultRow(RowKey.createRowKey(i++), t));
                    } catch (final InterruptedException exc) {
                        // TODO Auto-generated catch block
                        exc.printStackTrace();
                    }
                }
            };

            // TODO potentially multi-threading etc... or is this rather an
            // executor task?
            DataRow input;
            float ctr = 0;
            while ((input = rowInput.poll()) != null) {
                mapper.accept(input, consumer);
                if (totalRows > 0)
                    exec.setProgress(ctr++ / totalRows);

                exec.checkCanceled();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        for (final SettingsModel model : models.values()) {
            model.validateSettings(settings);
        }
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        for (final SettingsModel model : models.values()) {
            model.loadSettingsFrom(settings);
        }
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        for (final SettingsModel model : models.values()) {
            model.saveSettingsTo(settings);
        }
    }

    // Streaming
    @Override
    public InputPortRole[] getInputPortRoles() {
        return new InputPortRole[] { InputPortRole.DISTRIBUTED_STREAMABLE };
    }

    @Override
    public OutputPortRole[] getOutputPortRoles() {
        return new OutputPortRole[] { OutputPortRole.DISTRIBUTED };
    }

    // FIXME must be super performant
    @Override
    public StreamableOperator createStreamableOperator(
            final PartitionInfo partitionInfo, final PortObjectSpec[] inSpecs)
                    throws InvalidSettingsException {
        return new StreamableOperator() {

            @Override
            public void runFinal(final PortInput[] inputs,
                    final PortOutput[] outputs, final ExecutionContext exec)
                            throws Exception {
                run((RowInput) inputs[0], (RowOutput) outputs[0], exec, -1);
            }
        };
    }

    @Override
    protected void reset() {
        /* nothing to do */
    }

    // --- loading and saving ---
    @Override
    protected void loadInternals(final File nodeInternDir,
            final ExecutionMonitor exec)
                    throws IOException, CanceledExecutionException {
        /* nothing to do */
    }

    @Override
    protected void saveInternals(final File nodeInternDir,
            final ExecutionMonitor exec)
                    throws IOException, CanceledExecutionException {
        /* nothing to do */
    }
}
