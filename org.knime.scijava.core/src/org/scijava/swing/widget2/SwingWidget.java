package org.scijava.swing.widget2;

import javax.swing.JPanel;

import org.scijava.widget2.Widget;

public interface SwingWidget extends Widget, UIComponent<JPanel> {

	@Override
	default Class<JPanel> getComponentType() {
		return JPanel.class;
	}
}
