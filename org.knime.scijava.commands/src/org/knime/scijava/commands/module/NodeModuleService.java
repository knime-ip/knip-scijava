package org.knime.scijava.commands.module;

import java.util.Map;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.service.SciJavaService;

/**
 * Interface for services which are able to create {@link DataTableSpec}s from
 * ModuleInfos and is able to create NodeModules.
 *
 * @author Christian Dietz, University of Konstanz
 */
public interface NodeModuleService extends SciJavaService {

    /**
     * Create a output {@link DataTableSpec} from a {@link ModuleInfo}.
     *
     * @param info
     *            module info to create {@link DataTableSpec} from.
     * @param models
     *            {@link SettingsModel}s for given info.
     * @param inSpec
     *            Input {@link DataTableSpec}.
     * @return the created spec.
     */
    DataTableSpec createOutSpec(final ModuleInfo info,
            final Map<String, SettingsModel> models,
            final DataTableSpec inSpec);

    /**
     * Get the current output {@link ModuleItem} to {@link DataType} mapping.
     *
     * @param info
     *            The module info to get the output mapping for.
     * @param models
     *            {@link SettingsModel}s for given info.
     * @return A Map of the module items and the respective {@link DataType} of
     *         the output column to which they should be mapped.
     */
    Map<ModuleItem<?>, DataType> getOutputMapping(final ModuleInfo info,
            final Map<String, SettingsModel> models);

    /**
     * Create a NodeModule from the module info, a executable wrapper around the
     * module info for KNIME.
     *
     * @param info
     *            the module info to create a NodeModule for.
     * @param models
     *            settings models for {@link ModuleItem}s in info
     * @param spec
     *            Input data table spec
     * @param knimeLogger
     *            KNIME {@link NodeLogger} to delegate output to.
     * @return the {@link NodeModule}
     */
    NodeModule createNodeModule(final ModuleInfo info,
            final Map<String, SettingsModel> models, final DataTableSpec spec,
            NodeLogger knimeLogger);

    /**
     * @param info
     *            the module info
     * @return Whether the given Module info has a MultiOutputListener
     */
    boolean hasMultiOutputListener(final ModuleInfo info);

    /**
     * Get an id for the {@link ModuleInfo}.
     *
     * @param info
     *            the module info
     * @return the id
     */
    String getModuleInfoId(final ModuleInfo info);

    /**
     * Get the module info for the given id.
     *
     * @param id
     *            the id
     * @return the module info or <code>null</code> if the id was invalid
     */
    ModuleInfo getModuleInfoFromId(final String id);

}
