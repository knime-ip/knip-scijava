package org.knime.scijava.commands.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataType;
import org.knime.core.node.ExecutionContext;
import org.knime.scijava.commands.CellOutput;
import org.knime.scijava.commands.MultiOutputListener;
import org.knime.scijava.commands.converter.KNIMEConverterService;
import org.scijava.Context;
import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.module.ModuleService;
import org.scijava.module.process.ModulePreprocessor;
import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.PluginService;

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
     */
    public DefaultNodeModule(final Context context, final ModuleInfo info,
            final Map<String, Object> params,
            final Map<Integer, ModuleItem<?>> inputMapping,
            final Map<ModuleItem<?>, DataType> outputMapping) {
        context.inject(this);

        this.inputMapping = inputMapping;
        this.outputMapping = outputMapping;
        this.module = ms.createModule(info);

        // Setting parameters
        for (final Entry<String, Object> entry : params.entrySet()) {
            module.setInput(entry.getKey(), entry.getValue());
            module.resolveInput(entry.getKey());
        }

        // FIXME: do we need them all?
        // FIXME add our own preprocessors, i.e. to call preprocess?
        // FIXME or move this to "preprocess"
        final List<PreprocessorPlugin> pre = ps
                .createInstancesOfType(PreprocessorPlugin.class);

        if (pre != null) {
            for (final ModulePreprocessor p : pre) {
                p.process(module);
            }
        }

        for (final ModuleItem<?> item : info.inputs()) {
            if (MultiOutputListener.class.isAssignableFrom(item.getType())) {
                module.setInput(item.getName(),
                        outputListener = new NodeModuleOutputChangedListener());
            }
        }
    }

    /*
     * Pre process inputs
     *
     * @param input the input row
     */
    private void preProcess(final DataRow input) throws Exception {
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

    /*
     * Post process output of the module
     */
    private void postProcess(final CellOutput output,
            final ExecutionContext ctx) throws Exception {
        // output can be null if sink node
        if (output != null) {
            final List<DataCell> cells = new ArrayList<DataCell>();
            for (final ModuleItem<?> entry : module.getInfo().outputs()) {
                // FIXME hack because e.g. python script contains
                // result log
                if (!entry.getName().equals("result")) {
                    cells.add(cs.convertToKnime(
                            module.getOutput(entry.getName()), entry.getType(),
                            outputMapping.get(entry), ctx));
                }
            }
            output.push(cells.toArray(new DataCell[cells.size()]));
        }
    }

    @Override
    public void run(final DataRow input, final CellOutput output,
            final ExecutionContext ctx) throws Exception {
        // FIXME: Nicer logic possible here?
        preProcess(input);

        if (outputListener != null) {
            outputListener.setCellOutput(output);
            outputListener.setExecutionContext(ctx);
        }

        module.run();

        if (outputListener == null) {
            postProcess(output, ctx);
        }
    }

    // internal usage only
    class NodeModuleOutputChangedListener implements MultiOutputListener {

        private ExecutionContext ctx;

        private CellOutput output;

        public NodeModuleOutputChangedListener() {
        }

        @Override
        public void notifyListener() {
            try {
                postProcess(output, ctx);
            } catch (final Exception e) {
                // FIXME
                e.printStackTrace();
            }
        }

        public void setExecutionContext(final ExecutionContext ctx) {
            this.ctx = ctx;
        }

        public void setCellOutput(final CellOutput output) {
            this.output = output;
        }
    }
}
