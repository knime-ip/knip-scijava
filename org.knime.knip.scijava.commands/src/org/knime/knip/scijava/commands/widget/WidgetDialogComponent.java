package org.knime.knip.scijava.commands.widget;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.port.PortObjectSpec;
import org.scijava.ui.swing.widget.SwingInputWidget;

/**
 * A small DialogComponent wrapper around a {@link SwingInputWidget}.
 * 
 * @author Jonathan Hale
 */
public class WidgetDialogComponent extends DialogComponent {

	/* the wrapped InputWidget */
	private final SwingInputWidget<?> m_widget;

	/**
	 * Constructor.
	 * 
	 * @param widget
	 *            The {@link SwingInputWidget} to wrap
	 * @param model
	 *            The model which contains the settings model for the
	 *            InputWidget.
	 */
	public WidgetDialogComponent(SwingInputWidget<?> widget,
			DefaultKNIMEWidgetModel model) {
		super(model.getSettingsModel());

		m_widget = widget;
	}

	@Override
	protected void updateComponent() {
		m_widget.refreshWidget();
	}

	@Override
	protected void validateSettingsBeforeSave()
			throws InvalidSettingsException {
		m_widget.updateModel();
	}

	@Override
	protected void checkConfigurabilityBeforeLoad(PortObjectSpec[] specs)
			throws NotConfigurableException {
		/* nothing to do. */
	}

	@Override
	protected void setEnabledComponents(boolean enabled) {
		m_widget.getComponent().setEnabled(enabled);
	}

	@Override
	public void setToolTipText(String text) {
		m_widget.getComponent().setToolTipText(text);
	}

}
