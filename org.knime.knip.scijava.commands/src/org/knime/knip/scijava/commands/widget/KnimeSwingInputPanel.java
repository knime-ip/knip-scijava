/*
 * #%L
 * SciJava UI components for Java Swing.
 * %%
 * Copyright (C) 2010 - 2015 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package org.knime.knip.scijava.commands.widget;

import java.util.Collection;
import java.util.stream.Collectors;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.scijava.ui.swing.widget.SwingInputPanel;
import org.scijava.ui.swing.widget.SwingInputWidget;
import org.scijava.widget.InputWidget;

/**
 * Extension of the {@link SwingInputPanel} for KNIME dialogs. Additionally
 * provide a method to get {@link SettingsModel} which correspond to the
 * InputWidgets.
 * 
 * @author Jonathan Hale
 */
public class KnimeSwingInputPanel extends SwingInputPanel {

	/**
	 * @return SettingsModels of the {@link InputWidget}s in this panel
	 */
	public Collection<SettingsModel> getSettingsModels() {
		return widgets.values().stream()
				.filter((widget) -> widget
						.get() instanceof DefaultKNIMEWidgetModel)
				.map((widget) -> ((DefaultKNIMEWidgetModel) widget.get())
						.getSettingsModel())
				.collect(Collectors.toList());
	}

	/**
	 * @return Wrappers around the {@link SwingInputWidget}s with
	 *         {@link DefaultKNIMEWidgetModel} in this InputPanel.
	 */
	public Collection<DialogComponent> createDialogComponents() {
		return widgets.values().stream()
				.filter((widget) -> widget
						.get() instanceof DefaultKNIMEWidgetModel)
				.map((widget) -> new WidgetDialogComponent(
						(SwingInputWidget<?>) widget,
						((DefaultKNIMEWidgetModel) widget.get())))
				.collect(Collectors.toList());
	}

}
