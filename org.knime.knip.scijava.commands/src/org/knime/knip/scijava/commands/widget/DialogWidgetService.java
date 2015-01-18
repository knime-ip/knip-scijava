package org.knime.knip.scijava.commands.widget;

import java.util.List;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.scijava.plugin.Plugin;
import org.scijava.service.SciJavaService;
import org.scijava.widget.WidgetService;

/**
 * Interface for services that create {@link DialogInputWidget}s from
 * WidgetModels.
 * 
 * <p>
 * DialogWidgetService plugins discoverable at runtime must implement this
 * interface and be annotated with @{@link Plugin} with attribute
 * {@link Plugin#type()} = {@link DialogWidgetService}.class.
 * </p>
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
public interface DialogWidgetService extends
		WidgetService, SciJavaService {

	/**
	 * Get a list of createdWidgets.
	 * @return a List of DialogInputWidget
	 */
	List<DialogComponent> getDialogComponents();
	
	/**
	 * Clear list of created Widgets.
	 */
	void clearWidgets();
}
