/*
 * #%L
 * SciJava Common shared library for SciJava software.
 * %%
 * Copyright (C) 2009 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
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

import java.util.List;

import org.scijava.Priority;
import org.scijava.log.LogService;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.AbstractWrapperService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.widget.DefaultWidgetService;
import org.scijava.widget.InputPanel;
import org.scijava.widget.InputWidget;
import org.scijava.widget.WidgetModel;
import org.scijava.widget.WidgetService;

/**
 * Default service for managing available {@link InputWidget}s.
 * 
 * @author Curtis Rueden
 */
@Plugin(type = WidgetService.class, priority = Priority.HIGH_PRIORITY)
public class KnimeWidgetService extends
	AbstractWrapperService<WidgetModel, InputWidget<?, ?>> implements
	WidgetService
{

	@Parameter
	private LogService log;
	
	@Parameter
	private DefaultWidgetService widgetService;

	// -- WidgetService methods --

	@Override
	public WidgetModel createModel(InputPanel<?, ?> inputPanel, Module module,
		ModuleItem<?> item, List<?> objectPool)
	{
		return new DefaultKnimeWidgetModel(getContext(), inputPanel, module, item,
			objectPool);
	}

	// -- WrapperService methods --

	@Override
	public InputWidget<?, ?> create(final WidgetModel model) {
		return widgetService.create(model);
	}

	// -- PTService methods --

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public Class<InputWidget<?, ?>> getPluginType() {
		return (Class) InputWidget.class;
	}

	// -- Typed methods --

	@Override
	public Class<WidgetModel> getType() {
		return WidgetModel.class;
	}

}
