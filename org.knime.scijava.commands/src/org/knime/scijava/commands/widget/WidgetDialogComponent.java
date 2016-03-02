package org.knime.scijava.commands.widget;

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
	private final DialogInputWidgetModel m_model;

	/**
	 * Constructor.
	 *
	 * @param widget
	 *            The {@link SwingInputWidget} to wrap
	 * @param model
	 *            The model which contains the settings model for the
	 *            InputWidget.
	 */
	public WidgetDialogComponent(final SwingInputWidget<?> widget,
			final DialogInputWidgetModel model) {
		super(model.getSettingsModel());

		m_widget = widget;
		m_model = model;

		updateComponent();
	}

	@Override
	protected void updateComponent() {
		m_widget.refreshWidget();
		m_model.updateFromSettingsModel();
	}

	@Override
	protected void validateSettingsBeforeSave()
			throws InvalidSettingsException {
		m_widget.updateModel();
	}

	@Override
	protected void checkConfigurabilityBeforeLoad(final PortObjectSpec[] specs)
			throws NotConfigurableException {
		/* nothing to do. */
	}

	@Override
	protected void setEnabledComponents(final boolean enabled) {
		m_widget.getComponent().setEnabled(enabled);
	}

	@Override
	public void setToolTipText(final String text) {
		m_widget.getComponent().setToolTipText(text);
	}

}
