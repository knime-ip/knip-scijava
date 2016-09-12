package org.knime.scijava.commands.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.convert.datacell.JavaToDataCellConverterFactory;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
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
            final Map<String, SettingsModel> models, final DataTableSpec spec) {

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
                outputMapping);

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

        // TODO (What?)... What?
        final List<DataColumnSpec> tableSpecs = new ArrayList<>();

        // FIXME (result)
        for (final ModuleItem<?> item : info.outputs()) {
            if (item.getName().equals("result")) {
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
            final Collection<JavaToDataCellConverterFactory<?>> sourceFacs = cs
                    .getMatchingFactories(item.getType());
            for (final JavaToDataCellConverterFactory<?> fac : sourceFacs) {
                final String type = ((SettingsModelString) models
                        .get(item.getName())).getStringValue();
                if (fac.getDestinationType().getCellClass().getName()
                        .equals(type)) {
                    outputMapping.put(item, fac.getDestinationType());
                    break;
                }
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
