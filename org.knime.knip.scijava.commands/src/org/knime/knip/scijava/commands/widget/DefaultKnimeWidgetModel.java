package org.knime.knip.scijava.commands.widget;

import java.util.List;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.knip.scijava.commands.settings.NodeSettingsService;
import org.scijava.Context;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.widget.DefaultWidgetModel;
import org.scijava.widget.InputPanel;

/**
 * Default implementation of DialoginputWidgetModel.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
public class DefaultKnimeWidgetModel extends DefaultWidgetModel implements DialogInputWidgetModel {

	@Parameter
	private NodeSettingsService m_settingsService;
	
	public DefaultKnimeWidgetModel(Context context,
			InputPanel<?, ?> inputPanel, Module module, ModuleItem<?> item,
			List<?> objectPool) {
		super(context, inputPanel, module, item, objectPool);
		
		m_settingsService.createSettingsModel(item);
	}
	
	@Override
	public void updateModel() {
		updateToSettingsModel();
	}
	
	@Override
	public SettingsModel getSettingsModel() {
		return m_settingsService.getSettingsModel(getItem());
	}
	
	@Override
	public void updateToSettingsModel() {
		setValue(m_settingsService.getValue(getItem()));
	}
	
	@Override
	public void updateComponent() {
		setValue(m_settingsService.getValue(getItem()));
		updateModel();
	}

	@Override
	public void validateSettingsBeforeSave() throws InvalidSettingsException {
		m_settingsService.setValue(getItem(), getValue());
		return;
	}

}
