package org.knime.knip.scijava.commands.widget;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.port.PortObjectSpec;
import org.scijava.Context;
import org.scijava.Prioritized;
import org.scijava.plugin.PluginInfo;
import org.scijava.widget.InputWidget;
import org.scijava.widget.WidgetModel;
import org.scijava.widget.WidgetService;

public class DialogComponentWidget<T, W> extends DialogComponent implements DialogInputWidget<T, W> {

	private DialogInputWidgetModel m_model;
	private WidgetService m_widgetService;
	private InputWidget<T, W> m_widget;
	
	public DialogComponentWidget(DialogInputWidgetModel widgetModel) {
		super(widgetModel.getSettingsModel());
		
		m_model = widgetModel;
		
		m_widget = (InputWidget<T, W>) m_widgetService.create(widgetModel);
	}

	@Override
	protected void updateComponent() {
		m_model.updateComponent();
	}

	@Override
	protected void validateSettingsBeforeSave() throws InvalidSettingsException {
		m_model.validateSettingsBeforeSave();
	}

	@Override
	protected void checkConfigurabilityBeforeLoad(PortObjectSpec[] specs)
			throws NotConfigurableException {
		return;
	}

	@Override
	protected void setEnabledComponents(boolean enabled) {
		return;
	}

	@Override
	public void setToolTipText(String text) {
		m_model.getText();
	}

	@Override
	public WidgetModel get() {
		return m_model;
	}

	@Override
	public T getValue() {
		return (T) m_model.getValue();
	}

	@Override
	public boolean isLabeled() {
		return m_widget.isLabeled();
	}

	@Override
	public boolean isMessage() {
		return m_widget.isMessage();
	}

	@Override
	public void refreshWidget() {
		m_model.updateToSettingsModel();
		m_widget.refreshWidget();
	}

	@Override
	public void set(WidgetModel model) {
		if (model instanceof DialogInputWidgetModel){
			m_model = (DialogInputWidgetModel) model;
		}
	}

	@Override
	public boolean supports(WidgetModel model) {
		return (model instanceof DialogInputWidget) && (m_widget.supports(model));
	}

	@Override
	public void updateModel() {
		m_model.updateModel();
		m_widget.updateModel();
	}

	@Override
	public Context context() {
		return getContext();
	}

	@Override
	public Context getContext() {
		return m_widget.context();
	}

	@Override
	public void setContext(Context context) {
		m_widget.setContext(context);
	}

	@Override
	public double getPriority() {
		return m_widget.getPriority();
	}

	@Override
	public void setPriority(double priority) {
		m_widget.setPriority(priority);
	}

	@Override
	public int compareTo(Prioritized o) {
		return m_widget.compareTo(o);
	}

	@Override
	public PluginInfo<?> getInfo() {
		return m_widget.getInfo();
	}

	@Override
	public void setInfo(PluginInfo<?> info) {
		m_widget.setInfo(info);
	}

	@Override
	public Class getType() {
		return InputWidget.class;
	}

	@Override
	public W getComponent() {
		return m_widget.getComponent();
	}

	@Override
	public Class getComponentType() {
		return m_widget.getComponentType();
	}

	@Override
	public SettingsModel getSettingsModel() {
		return m_model.getSettingsModel();
	}

	@Override
	public void updateToSettingsModel() {
		m_model.updateToSettingsModel();
	}

}
