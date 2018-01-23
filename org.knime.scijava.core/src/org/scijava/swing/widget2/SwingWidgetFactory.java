package org.scijava.swing.widget2;

import org.scijava.widget2.WidgetFactory;

public interface SwingWidgetFactory extends WidgetFactory<SwingWidget> {

	@Override
	default Class<SwingWidget> widgetType() {
		return SwingWidget.class;
	}
}
