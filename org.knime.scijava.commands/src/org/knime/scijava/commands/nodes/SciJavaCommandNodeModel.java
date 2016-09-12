package org.knime.scijava.commands.nodes;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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
import org.knime.scijava.commands.CellOutput;
import org.knime.scijava.commands.module.NodeModule;
import org.knime.scijava.commands.module.NodeModuleService;
import org.scijava.Context;
import org.scijava.module.ModuleInfo;
import org.scijava.plugin.Parameter;

/**
 * NodeModel of ScijavaCommandNodes.
 *
 * @author Christian Dietz, University of Konstanz
 * @see SciJavaNodeSetFactory
 */
public class SciJavaCommandNodeModel extends NodeModel {

    @Parameter
    private NodeModuleService nodeService;

    private final ModuleInfo info;

    private final Map<String, SettingsModel> models;

    private Context context;

    protected SciJavaCommandNodeModel(final Context ctx, final ModuleInfo info,
            final Map<String, SettingsModel> models, final int numInports,
            final int numOutports) {
        super(numInports, numOutports);
        setContext(ctx);
        this.info = info;
        this.models = models;
    }

    private void setContext(final Context ctx) {
        this.context = ctx;
        context.inject(this);
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        if (getNrOutPorts() == 0) {
            return null;
        } else {
            return new DataTableSpec[] { nodeService.createOutSpec(info, models,
                    getNrInPorts() == 0 ? null : inSpecs[0]) };
        }
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

        final DataTableSpec inSpec = getNrInPorts() == 0 ? null
                : inData[0].getSpec();

        final BufferedDataTableRowOutput rowOutput;
        if (getNrOutPorts() != 0) {
            rowOutput = new BufferedDataTableRowOutput(exec.createDataContainer(
                    nodeService.createOutSpec(info, models, inSpec)));
        } else {
            rowOutput = null;
        }

        run(inSpec,
                getNrInPorts() == 0 ? null : new DataTableRowInput(inData[0]),
                rowOutput, exec, inData.length > 0 ? inData[0].size() : -1);

        rowOutput.close();

        return new BufferedDataTable[] { rowOutput.getDataTable() };
    }

    private void run(final DataTableSpec spec, final RowInput rowInput,
            final RowOutput rowOutput, final ExecutionContext exec,
            final long totalRows) {
        try {

            final KeyGenerator keyGen = createKeyGenerator(rowInput, rowOutput);

            final NodeModule module = nodeService.createNodeModule(info, models,
                    spec);

            CellOutput output = null;
            if (getNrOutPorts() > 0) {
                output = new CellOutput() {

                    @Override
                    public void push(final DataCell[] cells) {
                        try {
                            rowOutput.push(
                                    new DefaultRow(keyGen.create(), cells));
                        } catch (final InterruptedException e) {
                            // FIXME
                            e.printStackTrace();
                        }
                    }
                };
            }

            if (rowInput != null) {
                DataRow input;
                float ctr = 0;
                while ((input = rowInput.poll()) != null) {
                    keyGen.setCurrentKey(input.getKey());
                    module.run(input, output, exec);
                    exec.setProgress(ctr++ / totalRows);
                    exec.checkCanceled();
                }
            } else {
                module.run(null, output, exec);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    private KeyGenerator createKeyGenerator(final RowInput rowInput,
            final RowOutput rowOutput) {
        if (nodeService.hasMultiOutputListener(info)) {
            return new KeyGenerator() {
                private RowKey curr;
                private long ctr = 0;

                @Override
                public RowKey create() {
                    if (curr == null) {
                        return RowKey.createRowKey(ctr++);
                    } else {
                        return new RowKey(curr.getString() + "#" + ctr++);
                    }
                }

                @Override
                public void setCurrentKey(final RowKey curr) {
                    this.curr = curr;
                }
            };
        } else {
            return new KeyGenerator() {
                private RowKey curr;
                private long ctr = 0;

                @Override
                public RowKey create() {
                    if (curr == null) {
                        return RowKey.createRowKey(ctr++);
                    } else {
                        return new RowKey(curr.getString());
                    }
                }

                @Override
                public void setCurrentKey(final RowKey curr) {
                    this.curr = curr;
                }
            };
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
        if (getNrInPorts() == 1) {
            return new InputPortRole[] { InputPortRole.DISTRIBUTED_STREAMABLE };
        } else {
            return new InputPortRole[0];
        }
    }

    @Override
    public OutputPortRole[] getOutputPortRoles() {
        if (getNrOutPorts() == 1) {
            return new OutputPortRole[] { OutputPortRole.DISTRIBUTED };
        } else {
            return new OutputPortRole[0];
        }
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
                // FIXME avoid recreation of module in each run
                run((DataTableSpec) inSpecs[0],
                        getNrInPorts() == 0 ? null : (RowInput) inputs[0],
                        (RowOutput) outputs[0], exec, -1);
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
