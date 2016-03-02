package org.knime.scijava.commands.widget;

import java.util.List;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.scijava.commands.settings.NodeDialogSettingsService;
import org.scijava.Context;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.widget.DefaultWidgetModel;
import org.scijava.widget.InputPanel;

public class ColumnSelectKNIMEWidgetModel extends DefaultWidgetModel
		implements DialogInputWidgetModel {

	private final SettingsModelString m_model;
	@Parameter
	private NodeDialogSettingsService m_settingsService;

	public ColumnSelectKNIMEWidgetModel(final Context context,
			final InputPanel<?, ?> inputPanel, final Module module,
			final ModuleItem<?> item, final List<?> objectPool,
			final SettingsModelString settingsModelString) {
		super(context, inputPanel, module, item, objectPool);
		m_model = settingsModelString;
	}

	@Override
	public void setValue(final Object value) {
		m_model.setStringValue((String) value);
	};

	@Override
	public SettingsModel getSettingsModel() {
		return m_model;
	}

	@Override
	public void updateFromSettingsModel() {
		// not needed, handled in the refresh method of the
		// ColumnSelectionWidget
	}
}
