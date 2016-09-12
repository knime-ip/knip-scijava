package org.knime.scijava.commands.settings;

import java.util.HashMap;
import java.util.Map;

import org.knime.core.data.convert.util.ClassUtil;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.scijava.commands.StyleHook;
import org.knime.scijava.commands.converter.ConverterCacheService;
import org.knime.scijava.commands.settings.types.SettingsModelColumnSelectionType;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.AbstractSingletonService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Straight forward, rather ineffective default implementation of
 * SettingsModelTypeService.
 *
 * Ineffective because {@link #getSettingsModelTypeFor(SettingsModel)} uses
 * linear search to find a matching SettingsModelType.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@SuppressWarnings("rawtypes")
@Plugin(type = SettingsModelTypeService.class)
public class DefaultSettingsModelTypeService
        extends AbstractSingletonService<SettingsModelType>
        implements SettingsModelTypeService {

    @Parameter
    private ConverterCacheService cs;

    private final Map<Class<? extends SettingsModel>, SettingsModelType> m_pluginsByModel = new HashMap<>();
    private final Map<Class<?>, SettingsModelType> m_pluginsByValue = new HashMap<>();

    @Override
    public SettingsModelType getSettingsModelTypeFor(
            final SettingsModel settingsModel) {

        final SettingsModelType plugin = m_pluginsByModel
                .get(settingsModel.getClass());
        if (plugin != null) {
            return plugin;
        }

        for (final SettingsModelType p : getInstances()) {
            if (p.getSettingsModelClass().isInstance(settingsModel)) {
                m_pluginsByModel.put(settingsModel.getClass(), p);
                return p;
            }
        }
        // nothing found
        return null;
    }

    @Override
    public SettingsModelType getSettingsModelTypeFor(final ModuleItem<?> item) {

        // FIXME why do I need this check in case of scripts?... Why should you
        // not?
        if (item.getWidgetStyle() != null
                && item.getWidgetStyle().equals(StyleHook.COLUMNSELECTION)) {
            return new SettingsModelColumnSelectionType();
        } else {

            // check cache
            final SettingsModelType plugin = m_pluginsByValue
                    .get(item.getType());
            if (plugin != null) {
                return plugin;
            }

            // check primitive conversion cache

            // search
            for (final SettingsModelType<?, ?> p : getInstances()) {
                if (p instanceof SettingsModelColumnSelectionType) {
                    continue;
                }
                if (p.getValueClass().isAssignableFrom(
                        ClassUtil.ensureObjectType(item.getType()))) {
                    m_pluginsByValue.put(item.getType(), p);
                    return p;
                }
            }

            // check for column selection
            if (cs.getMatchingInputValueClass(item.getType()).isPresent()) {
                return new SettingsModelColumnSelectionType();
            }
        }

        // nothing found
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getValueFrom(final SettingsModel model) {
        return getSettingsModelTypeFor(model).getValue(model);
    }

    @Override
    public Class<SettingsModelType> getPluginType() {
        return SettingsModelType.class;
    }

}
