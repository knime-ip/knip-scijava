package org.knime.scijava.commands.settings;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.scijava.commands.converter.KNIMEConverterService;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

/**
 * Default implementation of {@link NodeSettingsService}
 *
 * @author Christian Dietz, University of Konstanz
 */
@Plugin(type = NodeSettingsService.class)
public class DefaultNodeSettingsService extends AbstractService
        implements NodeSettingsService {

    @Parameter
    private KNIMEConverterService cs;

    @Parameter
    private SettingsModelTypeService typeService;

    @Override
    public Object getValue(final SettingsModel model) {
        return typeService.getValueFrom(model);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public SettingsModel createSettingsModel(final ModuleItem<?> item) {
        SettingsModel sm = null;

        if (item.isInput()) {
            final SettingsModelType t = typeService
                    .getSettingsModelTypeFor(item);
            if (t != null) {
                // get default value
                final Object value = item.getDefaultValue();
                if (value != null) {
                    sm = t.create(item.getName(), value);
                    // set to default value
                    t.setValue(sm, value);
                } else {
                    sm = t.create(item.getName(), item.getMinimumValue());
                }
            }
        } else {
            sm = new SettingsModelString(item.getName(),
                    cs.getMatchingFactories(item.getType()).iterator().next()
                            .getDestinationType().getCellClass().getName());
        }

        return sm;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(final SettingsModel settingsModel,
            final Object value) {
        typeService.getSettingsModelTypeFor(settingsModel)
                .setValue(settingsModel, value);
    }
}
