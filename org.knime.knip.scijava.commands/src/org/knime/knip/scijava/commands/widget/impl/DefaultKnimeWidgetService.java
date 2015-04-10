package org.knime.knip.scijava.commands.widget.impl;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.knip.scijava.commands.settings.NodeSettingsService;
import org.knime.knip.scijava.commands.widget.DefaultKnimeWidgetModel;
import org.knime.knip.scijava.commands.widget.DialogComponentWidget;
import org.knime.knip.scijava.commands.widget.DialogInputWidget;
import org.knime.knip.scijava.commands.widget.DialogInputWidgetModel;
import org.knime.knip.scijava.commands.widget.DialogWidgetService;
import org.scijava.log.LogService;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.AbstractWrapperService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.widget.InputPanel;
import org.scijava.widget.InputWidget;
import org.scijava.widget.WidgetModel;
import org.scijava.widget.WidgetService;

/**
 * Default implementation of DialogWidgetService for DialogInputWidgetModels.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
@Plugin(type = WidgetService.class)
public class DefaultKnimeWidgetService extends
		AbstractWrapperService<WidgetModel, InputWidget<?, ?>> implements
		DialogWidgetService {

	@Parameter
	private NodeSettingsService m_settingsService;

	@Parameter
	private WidgetService m_widgetService;

	@Parameter
	private LogService m_log;

	private ArrayList<DialogComponent> m_widgets = new ArrayList<DialogComponent>();

	@Override
	public DialogInputWidget<?, ?> create(WidgetModel model) {
		if (model instanceof DialogInputWidgetModel) {
			InputWidget<?, ?> widget = m_widgetService.create(model);
			if (widget == null) {
				return null;
			}
			DialogComponentWidget w = new DialogComponentWidget(
					(DialogInputWidgetModel) model);
			m_widgets.add(w);

			return w;
		}
		m_log.warn("No widget found for input: " + model.getItem().getName());
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<InputWidget<?, ?>> getPluginType() {
		return (Class) InputWidget.class;
	}

	@Override
	public Class<WidgetModel> getType() {
		return WidgetModel.class;
	}

	@Override
	public List<DialogComponent> getDialogComponents() {
		return m_widgets;
	}

	@Override
	public void clearWidgets() {
		m_widgets.clear();
	}

	@Override
	public WidgetModel createModel(InputPanel<?, ?> inputPanel, Module module,
			ModuleItem<?> item, List<?> objectPool) {
		return new DefaultKnimeWidgetModel(getContext(), inputPanel, module,
				item, objectPool);
	}

}