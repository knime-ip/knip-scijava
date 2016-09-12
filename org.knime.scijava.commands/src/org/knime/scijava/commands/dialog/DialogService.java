package org.knime.scijava.commands.dialog;

import java.util.List;

import javax.swing.JPanel;

import org.knime.core.util.Pair;
import org.knime.scijava.commands.widget.SettingsModelWidgetModel;
import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.service.SciJavaService;

/**
 * Interface for services which create dialog panels.
 *
 * @author Christian Dietz, University of Konstanz
 */
public interface DialogService extends SciJavaService {

    /**
     * Create a dialog panel for the given module.
     *
     * @param module
     *            the module
     * @return the created input panel and a list of
     *         {@link SettingsModelWidgetModel}s used for it.
     */
    Pair<JPanel, List<SettingsModelWidgetModel>> dialogPanel(
            final Module module);

    /**
     * Create a dialog panel for the given module info.
     *
     * @param info
     *            the module info
     * @return the created input panel and a list of
     *         {@link SettingsModelWidgetModel}s used for it.
     */
    Pair<JPanel, List<SettingsModelWidgetModel>> dialogPanel(
            final ModuleInfo info);
}
