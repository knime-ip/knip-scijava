package org.knime.scijava.commands.widget;

import java.util.List;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.scijava.commands.settings.NodeDialogSettingsService;
import org.knime.scijava.commands.settings.NodeSettingsService;
import org.scijava.Context;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.widget.DefaultWidgetModel;
import org.scijava.widget.InputPanel;

/**
 * Default implementation of DialogInputWidgetModel.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
public class DefaultKNIMEWidgetModel extends DefaultWidgetModel
		implements DialogInputWidgetModel {

	@Parameter
	private NodeDialogSettingsService m_settingsService;
	private final SettingsModel m_model;

	/**
	 * Constructor for generic input items. The used SettingsModel will be
	 * created by a {@link NodeSettingsService}.
	 *
	 * @see DialogInputWidgetModel
	 * @param context
	 *            Context for the model
	 * @param inputPanel
	 *            the panel
	 * @param module
	 *            the module
	 * @param item
	 *            the module item
	 * @param objectPool
	 *            the ObejctPool
	 */
	public DefaultKNIMEWidgetModel(final Context context,
			final InputPanel<?, ?> inputPanel, final Module module,
			final ModuleItem<?> item, final List<?> objectPool) {
		super(context, inputPanel, module, item, objectPool);

		m_model = m_settingsService.createAndAddSettingsModel(item, module,
				false);
		updateFromSettingsModel();
	}

	@Override
	public void setValue(final Object value) {
		// keep track of the values, update settings model
		m_settingsService.setValue(getItem(), value);
	}

	@Override
	public SettingsModel getSettingsModel() {
		return m_model;
	}

	@Override
	public void updateFromSettingsModel() {
		super.setValue(m_settingsService.getValue(getItem()));
	}
}
