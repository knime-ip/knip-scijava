package org.knime.knip.scijava.commands.widget.impl;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.knip.scijava.commands.settings.NodeSettingsService;
import org.knime.knip.scijava.commands.widget.DialogInputWidget;
import org.scijava.Context;
import org.scijava.Prioritized;
import org.scijava.plugin.PluginInfo;
import org.scijava.widget.InputWidget;
import org.scijava.widget.WidgetModel;

/**
 * Default implementation of KnimeDialogInputWidget which contains a general
 * InputWidget and maintains a SettingsModel.
 * 
 * This class does not need to be injected with a Context, instead a
 * {@link NodeSettingsService} is passed via the constructor. This is required
 * because this {@link DialogComponent}s {@link SettingsModel} needs to be
 * created in the constructor already.
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
public class WrappingKnimeDialogInputWidget<T, W> extends DialogComponent
		implements DialogInputWidget<T, W> {

	private InputWidget<T, W> m_widget;
	private NodeSettingsService m_settingsService;

	/**
	 * Constructor.
	 * 
	 * @param widget
	 *            the widget to use.
	 * @param settingsService
	 *            Service to create a SettingsModel from.
	 */
	public WrappingKnimeDialogInputWidget(InputWidget<T, W> widget,
			NodeSettingsService settingsService) {
		super(settingsService.createSettingsModel(widget.get().getItem()));
		m_widget = widget;
		m_settingsService = settingsService;
	}

	@Override
	public void updateModel() {
		updateToSettingsModel();
	}

	@Override
	public T getValue() {
		return m_widget.getValue();
	}

	@Override
	public void refreshWidget() {
		m_widget.refreshWidget();
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
	public void set(WidgetModel model) {
		m_widget.set(model);
	}

	@Override
	public WidgetModel get() {
		return m_widget.get();
	}

	@Override
	public boolean supports(WidgetModel model) {
		return m_widget.supports(model);
	}

	@Override
	public Context context() {
		return m_widget.context();
	}

	@Override
	public Context getContext() {
		return m_widget.getContext();
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
	public Class<WidgetModel> getType() {
		return m_widget.getType();
	}

	@Override
	public W getComponent() {
		return m_widget.getComponent();
	}

	@Override
	public Class<W> getComponentType() {
		return m_widget.getComponentType();
	}

	@Override
	public SettingsModel getSettingsModel() {
		return m_settingsService.getSettingsModel(m_widget.get().getItem());
	}

	@Override
	public void updateToSettingsModel() {
		m_widget.get().setValue(
				m_settingsService.getValue(m_widget.get().getItem()));
	}

	@Override
	protected void updateComponent() {
		m_widget.get().setValue(m_settingsService.getValue(m_widget.get().getItem()));
		updateModel();
	}

	@Override
	protected void validateSettingsBeforeSave() throws InvalidSettingsException {
		m_settingsService.setValue(m_widget.get().getItem(), m_widget.get()
				.getValue());
		return;
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
		return;
	}

}
