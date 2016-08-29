package org.knime.scijava.commands.dialog;

import java.util.List;

import javax.swing.JPanel;

import org.knime.core.util.Pair;
import org.knime.scijava.commands.widget.SettingsModelWidgetModel;
import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.service.SciJavaService;

public interface DialogService extends SciJavaService {

    Pair<JPanel, List<SettingsModelWidgetModel>> dialogPanel(
            final Module module);

    Pair<JPanel, List<SettingsModelWidgetModel>> dialogPanel(
            final ModuleInfo info);
}
