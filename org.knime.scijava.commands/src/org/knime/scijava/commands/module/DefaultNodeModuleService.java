package org.knime.scijava.commands.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.util.UniqueNameGenerator;
import org.knime.scijava.commands.MultiOutputListener;
import org.knime.scijava.commands.converter.KNIMEConverterService;
import org.knime.scijava.commands.settings.SettingsModelType;
import org.knime.scijava.commands.settings.SettingsModelTypeService;
import org.knime.scijava.commands.settings.models.SettingsModelColumnSelection;
import org.knime.scijava.commands.settings.types.SettingsModelColumnSelectionType;
import org.scijava.Identifiable;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.module.ModuleService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.script.ScriptInfo;
import org.scijava.service.AbstractService;

/**
 * Default implementation of {@link NodeModuleService}.
 *
 * @author Christian Dietz, University of Konstanz
 */
@Plugin(type = NodeModuleService.class)
public class DefaultNodeModuleService extends AbstractService
        implements NodeModuleService {

    @Parameter
    private SettingsModelTypeService ts;

    @Parameter
    private KNIMEConverterService cs;

    @Parameter
    private ModuleService ms;

    @Override
    public NodeModule createNodeModule(final ModuleInfo info,
            final Map<String, SettingsModel> models, final DataTableSpec spec,
            final NodeLogger knimeLogger) {

        final Map<Integer, ModuleItem<?>> inputMapping = new HashMap<>();
        final Map<String, Object> params = new HashMap<String, Object>();

        final Map<ModuleItem<?>, DataType> outputMapping = getOutputMapping(
                info, models);
        for (final ModuleItem<?> item : info.inputs()) {
            if (MultiOutputListener.class.isAssignableFrom(item.getType())) {
                continue;
            }

            final String name = item.getName();

            final SettingsModelType<?, ?> type = ts
                    .getSettingsModelTypeFor(item);

            // FIXME make this extensible? special handling for special
            // mappings or SettingsModels?
            if (type instanceof SettingsModelColumnSelectionType) {
                final SettingsModelColumnSelection settingsModel = (SettingsModelColumnSelection) models
                        .get(item.getName());
                inputMapping.put(
                        spec.findColumnIndex(settingsModel.getStringValue()),
                        item);
            } else {
                @SuppressWarnings("unchecked")
                final SettingsModelType<SettingsModel, ?> tmp = ((SettingsModelType<SettingsModel, ?>) type);
                params.put(name, tmp.getValue(models.get(name)));
            }
        }

        return new DefaultNodeModule(getContext(), info, params, inputMapping,
                outputMapping, knimeLogger);

    }

    @Override
    public DataTableSpec createOutSpec(final ModuleInfo info,
            final Map<String, SettingsModel> models,
            final DataTableSpec inSpec) {

        final Map<ModuleItem<?>, DataType> outputMapping = getOutputMapping(
                info, models);

        final UniqueNameGenerator nameGen;
        if (inSpec != null) {
            nameGen = new UniqueNameGenerator(inSpec);
        } else {
            nameGen = new UniqueNameGenerator(new HashSet<>());
        }

        final List<DataColumnSpec> tableSpecs = new ArrayList<>();

        final boolean hasSyntheticResult = info instanceof ScriptInfo &&
                ((ScriptInfo) info).isReturnValueAppended();
        for (final ModuleItem<?> item : info.outputs()) {
            if (!hasSyntheticResult && item.getName().equals("result")) {
                continue;
            }
            tableSpecs.add(
                    nameGen.newColumn(item.getName(), outputMapping.get(item)));
        }

        return new DataTableSpec(
                tableSpecs.toArray(new DataColumnSpec[tableSpecs.size()]));
    }

    @Override
    public Map<ModuleItem<?>, DataType> getOutputMapping(final ModuleInfo info,
            final Map<String, SettingsModel> models) {
        final HashMap<ModuleItem<?>, DataType> outputMapping = new HashMap<>();

        for (final ModuleItem<?> item : info.outputs()) {
            final Optional<DataType> type = cs
                    .getPreferredDataType(item.getType());

            if (type.isPresent()) {
                final DataType theType = type.get();
                outputMapping.put(item, theType);
            }
        }

        return outputMapping;
    }

    @Override
    public boolean hasMultiOutputListener(final ModuleInfo info) {
        for (final ModuleItem<?> item : info.inputs()) {
            if (MultiOutputListener.class.isAssignableFrom(item.getType())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getModuleInfoId(final ModuleInfo info) {

        if (info instanceof Identifiable) {
            return ((Identifiable) info).getIdentifier();
        }

        throw new IllegalStateException(
                "Only Identifiable ModuleInfos are supported, yet!");

    }

    @Override
    public ModuleInfo getModuleInfoFromId(final String id) {
        final ModuleInfo info = ms.getModuleById(id);
        if (info != null) {
            return info;
        }
        throw new IllegalStateException(
                "Only Identifiable ModuleInfos are supported, yet!");
    }
}
