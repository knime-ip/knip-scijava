package org.knime.scijava.commands.exp.template.multimap;

import org.knime.core.node.DynamicNodeFactory;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDescription;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeView;
import org.knime.core.node.config.ConfigRO;
import org.knime.core.node.config.ConfigWO;
import org.knime.scijava.commands.SciJavaGateway;
import org.knime.scijava.commands.module.NodeModuleService;
import org.knime.scijava.commands.nodes.SciJavaNodeDescription;
import org.knime.scijava.commands.nodes.SciJavaNodeSetFactory;
import org.knime.scijava.commands.settings.NodeSettingsService;
import org.scijava.plugin.Parameter;

/**
 * NodeModel of Scijava Command Nodes.
 *
 * @author Christian Dietz, University of Konstanz
 */
// FIXME Make abstract in the long run?
public class SciJavaMultiMapNodeFactory
        extends DynamicNodeFactory<SciJavaMultiMapNodeModel> {

    @Parameter
    private NodeSettingsService nss;

    @Parameter
    private NodeModuleService nms;

    public SciJavaMultiMapNodeFactory() {
        super();
        SciJavaGateway.get().getGlobalContext().inject(this);
    }

    @Override
    protected NodeDescription createNodeDescription() {
        return new SciJavaNodeDescription(info, 1, 1);
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
    public NodeView<SciJavaMultiMapNodeModel> createNodeView(final int viewIndex,
            final SciJavaMultiMapNodeModel nodeModel) {
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
        return new SciJavaMultiMapNodeDialog(SciJavaGateway.get().getGlobalContext(),
                info);
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
        // FIXME numViews etc..

        super.loadAdditionalFactorySettings(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SciJavaMultiMapNodeModel createNodeModel() {
        return new SciJavaMultiMapNodeModel(SciJavaGateway.get().getGlobalContext(),
                info);
    }

}
