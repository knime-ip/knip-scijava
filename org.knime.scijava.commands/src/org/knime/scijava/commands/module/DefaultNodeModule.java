package org.knime.scijava.commands.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataType;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.scijava.commands.CellOutput;
import org.knime.scijava.commands.MultiOutputListener;
import org.knime.scijava.commands.converter.KNIMEConverterService;
import org.knime.scijava.commands.log.KNIMELogService;
import org.scijava.Context;
import org.scijava.log.LogService;
import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.module.ModuleService;
import org.scijava.module.process.ModulePreprocessor;
import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.PluginService;
import org.scijava.script.ScriptInfo;

/**
 * Default implementation of {@link NodeModule}.
 *
 * @author Christian Dietz, University of Konstanz
 */
class DefaultNodeModule implements NodeModule {

    @Parameter
    private PluginService ps;

    @Parameter
    private KNIMEConverterService cs;

    @Parameter
    private ModuleService ms;

    // Internally used variables
    private final Module module;

    private final Map<Integer, ModuleItem<?>> inputMapping;

    private final Map<ModuleItem<?>, DataType> outputMapping;

    private NodeModuleOutputChangedListener outputListener;

    private final LogService logService;

    /**
     * Constructor.
     *
     * @param context
     *            Scijava context.
     * @param info
     *            info this module is created for
     * @param params
     *            preresolved values for the parameters of the module
     * @param inputMapping
     *            mapping of input column index to input items of the module
     * @param outputMapping
     *            mapping of output items to types for the cells to for them
     * @param logger
     *            NodeLogger to delegate output to.
     */
    public DefaultNodeModule(final Context context, final ModuleInfo info,
            final Map<String, Object> params,
            final Map<Integer, ModuleItem<?>> inputMapping,
            final Map<ModuleItem<?>, DataType> outputMapping,
            final NodeLogger logger) {
        context.inject(this);

        this.inputMapping = inputMapping;
        this.outputMapping = outputMapping;
        this.module = ms.createModule(info);
        this.outputListener = new NodeModuleOutputChangedListener(false);
        this.logService = new KNIMELogService(logger);

        preProcess(params);
    }

    /*
     * "Once per node execution" pre-processing. Called in constructor.
     *
     * @param params key-value pairs of pre resolved module inputs.
     */
    private void preProcess(final Map<String, Object> params) {
        // Setting parameters
        for (final Entry<String, Object> entry : params.entrySet()) {
            module.setInput(entry.getKey(), entry.getValue());
            module.resolveInput(entry.getKey());
        }

        // FIXME: do we need them all?
        final List<PreprocessorPlugin> pre = ps
                .createInstancesOfType(PreprocessorPlugin.class);

        for (final ModulePreprocessor p : pre) {
            p.process(module);
        }

        for (final ModuleItem<?> item : this.module.getInfo().inputs()) {
            if (MultiOutputListener.class.equals(item.getType())) {
                /* MultiOutputListener */
                final String name = item.getName();

                outputListener = new NodeModuleOutputChangedListener(true);
                module.setInput(name, outputListener);
                module.resolveInput(name);
            } else if (LogService.class.equals(item.getType())) {
                /* LogService */
                final String name = item.getName();

                module.setInput(name, logService);
                module.resolveInput(name);
            }
        }
    }

    /*
     * Set module input values from the input row.
     */
    private void setModuleInput(final DataRow input) throws Exception {
        // input can be null if source node
        if (input != null) {
            for (final Entry<Integer, ModuleItem<?>> entry : inputMapping
                    .entrySet()) {
                final Object obj = cs.convertToJava(
                        input.getCell(entry.getKey()),
                        entry.getValue().getType());
                module.setInput(entry.getValue().getName(), obj);
            }
        }
    }

    @Override
    public void run(final DataRow input, final CellOutput output,
            final ExecutionContext ctx) throws Exception {
        setModuleInput(input);

        outputListener.setCellOutput(output);
        outputListener.setExecutionContext(ctx);

        module.run();

        outputListener.flush();
    }

    private class NodeModuleOutputChangedListener
            implements MultiOutputListener {
        private ExecutionContext ctx;

        private CellOutput output;

        /* true if the modules handles pushes itself, false otherwise */
        private final boolean manualPush;

        /**
         * Constructor.
         *
         * @param manualPush
         *            if <code>true</code>, signals that the module handles
         *            pushing rows.
         */
        public NodeModuleOutputChangedListener(final boolean manualPush) {
            this.manualPush = manualPush;
        }

        @Override
        public void notifyListener() throws Exception {
            // output can be null if sink node
            if (output != null) {
                final boolean hasSyntheticResult = module.getInfo() instanceof ScriptInfo &&
                        ((ScriptInfo) module.getInfo()).isReturnValueAppended();

                final List<DataCell> cells = new ArrayList<DataCell>();
                for (final ModuleItem<?> entry : module.getInfo().outputs()) {
                    // FIXME hack because e.g. python script contains
                    // result log
                    if (!hasSyntheticResult || !entry.getName().equals("result")) {
                        cells.add(cs.convertToKnime(
                                module.getOutput(entry.getName()),
                                entry.getType(), outputMapping.get(entry),
                                ctx));
                    }
                }
                output.push(cells.toArray(new DataCell[cells.size()]));
            }
        }

        /**
         * Set KNIME Execution Context
         *
         * @param ctx
         *            execution context
         */
        public void setExecutionContext(final ExecutionContext ctx) {
            this.ctx = ctx;
        }

        /**
         * Set CellOutput to push rows to.
         *
         * @param output
         *            cell output
         */
        public void setCellOutput(final CellOutput output) {
            this.output = output;
        }

        /**
         * Final flush. Will push a row in case module does not handle pushing
         * output rows.
         * @throws Exception
         */
        public void flush() throws Exception {
            if (!manualPush) {
                notifyListener();
            }
        }
    }
}
