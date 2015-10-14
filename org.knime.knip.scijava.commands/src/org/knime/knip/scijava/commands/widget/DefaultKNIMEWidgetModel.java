package org.knime.knip.scijava.commands.widget;

import java.util.List;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.knip.scijava.commands.settings.NodeSettingsService;
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
	private NodeSettingsService settingsService;
	private final SettingsModel model;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            Context for the model
	 * @param inputPanel
	 * @param module
	 * @param item
	 * @param objectPool
	 */
	public DefaultKNIMEWidgetModel(final Context context,
			final InputPanel<?, ?> inputPanel, final Module module,
			final ModuleItem<?> item, final List<?> objectPool) {
		super(context, inputPanel, module, item, objectPool);

		model = settingsService.createAndAddSettingsModel(item);
		updateToSettingsModel();
	}

	@Override
	public void updateSettingsModel() {
	}

	@Override
	public void setValue(final Object value) {
		super.setValue(value);

		// keep track of the values, update settings model
		settingsService.setValue(getItem(), value);
	}

	@Override
	public SettingsModel getSettingsModel() {
		return model;
	}

	@Override
	public void updateToSettingsModel() {
		super.setValue(settingsService.getValue(getItem()));
	}

}
