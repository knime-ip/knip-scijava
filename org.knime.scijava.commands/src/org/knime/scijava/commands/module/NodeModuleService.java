package org.knime.scijava.commands.module;

import java.util.Map;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.service.SciJavaService;

public interface NodeModuleService extends SciJavaService {

    DataTableSpec createOutSpec(final ModuleInfo info,
            final Map<String, SettingsModel> models,
            final DataTableSpec inSpec);

    Map<ModuleItem<?>, DataType> getOutputMapping(ModuleInfo info,
            Map<String, SettingsModel> models);

    NodeModule createNodeModule(ModuleInfo info,
            Map<String, SettingsModel> models, DataTableSpec spec);

    boolean hasMultiOutputListener(final ModuleInfo info);

    String getModuleInfoId(final ModuleInfo info);

    ModuleInfo getModuleInfoFromId(String id);

}
