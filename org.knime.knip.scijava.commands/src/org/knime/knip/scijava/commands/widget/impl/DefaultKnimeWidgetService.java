package org.knime.knip.scijava.commands.widget.impl;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.knip.scijava.commands.settings.NodeSettingsService;
import org.knime.knip.scijava.commands.widget.DialogInputWidget;
import org.knime.knip.scijava.commands.widget.DialogWidgetService;
import org.scijava.plugin.AbstractWrapperService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.widget.InputWidget;
import org.scijava.widget.WidgetModel;
import org.scijava.widget.WidgetService;

@Plugin(type=WidgetService.class)
public class DefaultKnimeWidgetService extends
		AbstractWrapperService<WidgetModel, InputWidget<?, ?>> implements
		DialogWidgetService {

	@Parameter
	private NodeSettingsService m_settingsService;
	
	@Parameter
	private WidgetService m_widgetService;
	
	private ArrayList<DialogComponent> m_widgets = new ArrayList<DialogComponent>();
	
	@Override
	public DialogInputWidget<?, ?> create(WidgetModel model) {
		InputWidget<?, ?> widget = m_widgetService.create(model);
		if (widget == null) { 
			return null;
		}
		WrappingKnimeDialogInputWidget w = new WrappingKnimeDialogInputWidget<>(widget, m_settingsService);
		m_widgets.add(w);
		
		return w;
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

}