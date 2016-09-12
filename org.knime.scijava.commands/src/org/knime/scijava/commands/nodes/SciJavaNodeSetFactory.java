package org.knime.scijava.commands.nodes;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSetFactory;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.config.ConfigRO;
import org.knime.scijava.commands.SciJavaGateway;
import org.knime.scijava.commands.SciJavaTestCommand;
import org.knime.scijava.commands.module.NodeModuleService;
import org.scijava.MenuEntry;
import org.scijava.MenuPath;
import org.scijava.command.CommandService;
import org.scijava.module.ModuleInfo;
import org.scijava.plugin.Parameter;
import org.scijava.script.ScriptService;

/**
 * NodeSetFactory which creates Nodes for Scijava commands.
 *
 * @author Christian Dietz, University of Konstanz
 */
// FIXME Temporarily. Serves as an example. Maybe abstract in the future
public class SciJavaNodeSetFactory implements NodeSetFactory {

    public static final String SCIJAVA_COMMAND_KEY = "SCIJAVA_COMMAND_KEY";

    private static final String SCIJAVA_TEST_PATH = "/community/knip/scijava";

    // FIXME this is temporary. Will be moved to specialized NodeSetFactory
    @Parameter
    private ScriptService scripts;

    // FIXME this is temporary. Will be moved to specialized NodeSetFactory.
    @Parameter
    private CommandService cs;

    @Parameter
    private NodeModuleService nms;

    public SciJavaNodeSetFactory() {
        SciJavaGateway.get().getGlobalContext().inject(this);
    }

    private final HashMap<String, String> categories = new HashMap<String, String>();

    /*
     * Scan script directory for scripts and add test command
     */
    private List<ModuleInfo> discoverModules() {
        final List<ModuleInfo> infos = new ArrayList<>();

        // FIXME this hack should take place in the activator of the potential
        // new scripting plugin
        final ClassLoader curr = Thread.currentThread().getContextClassLoader();
        Thread.currentThread()
                .setContextClassLoader(SciJavaGateway.get().getClassLoader());
        scripts.addScriptDirectory(new File("/home/dietzc/Desktop/scripts/"),
                new MenuPath("SciJava>Test"));

        infos.addAll(scripts.getScripts());
        infos.add(cs.getCommand(SciJavaTestCommand.class));

        Thread.currentThread().setContextClassLoader(curr);

        return infos;
    }

    @Override
    public Collection<String> getNodeFactoryIds() {
        final List<ModuleInfo> moduleInfos = discoverModules();
        final List<String> moduleIds = new ArrayList<String>(
                moduleInfos.size());
        for (final ModuleInfo info : moduleInfos) {
            final String id = nms.getModuleInfoId(info);
            moduleIds.add(id);

            String path = "";
            if (info.getMenuPath().size() > 0) {
                String tmp = "";
                for (final MenuEntry entry : info.getMenuPath()) {
                    path += tmp; // omit last value from the menu
                    tmp = "/" + entry.getName();
                }
            }
            categories.put(id, SCIJAVA_TEST_PATH + path);
        }

        return moduleIds;
    }

    @Override
    public Class<? extends NodeFactory<? extends NodeModel>> getNodeFactory(
            final String id) {
        return SciJavaCommandNodeFactory.class;
    }

    @Override
    public String getCategoryPath(final String id) {
        return categories.get(id);
    }

    @Override
    public String getAfterID(final String id) {
        return "";
    }

    @Override
    public ConfigRO getAdditionalSettings(final String id) {
        // FIXME manage versions?
        final NodeSettings settings = new NodeSettings(
                "scijavacommand-factory");
        settings.addString(SCIJAVA_COMMAND_KEY, id);
        return settings;
    }

}
