package org.knime.scijava.commands.nodes;

import java.util.Map;
import java.util.Map.Entry;

import org.knime.core.node.DynamicNodeFactory;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDescription;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeView;
import org.knime.core.node.config.ConfigRO;
import org.knime.core.node.config.ConfigWO;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.scijava.commands.SciJavaGateway;
import org.knime.scijava.commands.module.NodeModuleService;
import org.knime.scijava.commands.settings.NodeSettingsService;
import org.knime.scijava.commands.settings.models.SettingsModelColumnSelection;
import org.scijava.module.ModuleInfo;
import org.scijava.plugin.Parameter;

/**
 * NodeModel of Scijava Command Nodes.
 *
 * @author Christian Dietz, University of Konstanz
 */
// FIXME Make abstract in the long run?
public class SciJavaCommandNodeFactory
        extends DynamicNodeFactory<SciJavaCommandNodeModel> {

    @Parameter
    private NodeSettingsService nss;

    @Parameter
    private NodeModuleService nms;

    private ModuleInfo info;

    private Map<String, SettingsModel> models;

    private int numOutports;

    private int numInports;

    public SciJavaCommandNodeFactory() {
        super();
        SciJavaGateway.get().getGlobalContext().inject(this);
    }

    @Override
    protected NodeDescription createNodeDescription() {
        return new SciJavaNodeDescription(info, numInports, numOutports);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getNrNodeViews() {
        // FIXME
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @return null (ScriptingNode does not have views.)
     */
    @Override
    public NodeView<SciJavaCommandNodeModel> createNodeView(final int viewIndex,
            final SciJavaCommandNodeModel nodeModel) {
        // FIXME
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NodeDialogPane createNodeDialogPane() {
        return new SciJavaCommandNodeDialog(
                SciJavaGateway.get().getGlobalContext(), info);
    }

    @Override
    public void saveAdditionalFactorySettings(final ConfigWO config) {
        config.addString(SciJavaNodeSetFactory.SCIJAVA_COMMAND_KEY,
                nms.getModuleInfoId(info));
        super.saveAdditionalFactorySettings(config);
    }

    @Override
    public void loadAdditionalFactorySettings(final ConfigRO config)
            throws InvalidSettingsException {
        info = nms.getModuleInfoFromId(
                config.getString(SciJavaNodeSetFactory.SCIJAVA_COMMAND_KEY));
        models = nss.getSettingsModels(info);
        numInports = getNrInPorts(info, models);
        numOutports = getNrOutPorts(info);

        // FIXME numViews etc..

        super.loadAdditionalFactorySettings(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SciJavaCommandNodeModel createNodeModel() {
        return new SciJavaCommandNodeModel(
                SciJavaGateway.get().getGlobalContext(), info, models,
                numInports, numOutports);
    }

    private static int getNrOutPorts(final ModuleInfo info) {
        return info.outputs().iterator().hasNext() ? 1 : 0;
    }

    private static int getNrInPorts(final ModuleInfo info,
            final Map<String, SettingsModel> map) {
        for (final Entry<String, SettingsModel> model : map.entrySet()) {
            if (model.getValue() instanceof SettingsModelColumnSelection) {
                return 1;
            }
        }
        return 0;
    }

}
