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
package org.knime.scijava.core;

import java.util.Collection;

import org.scijava.Context;
import org.scijava.NoSuchServiceException;
import org.scijava.plugin.PluginIndex;
import org.scijava.service.Service;
import org.scijava.service.ServiceHelper;
import org.scijava.service.ServiceIndex;
import org.scijava.util2.ClassUtils;

/**
 * SubContex is a context which allows a selection of services to be scoped
 * locally, while providing access to all Services of a super context. This
 * allows the cheap creation of local contexts without running into problems
 * with missing services.
 *
 * @author Gabriel Einsdorf
 *
 */
public class SubContext extends Context {

	/** the super context */
	private Context m_superContext;

	/** Index of the Subcontext's services. */
	private final ServiceIndex m_serviceIndex;

	/** Master index of all plugins handeled by this sub context. */
	private final PluginIndex m_pluginIndex;

	/**
	 * If the super context is searched in case a plugin can not be found
	 * locally.
	 */
	private boolean delegating = false;

	/**
	 * Creates a Context that keeps the given services local, while still
	 * providing access to the services of a super context.
	 *
	 * @param supercontext
	 *            the Context that provides access to more services.
	 * @param serviceClasses
	 *            the services this context will provide locally.
	 */
	@SuppressWarnings("rawtypes")
	public SubContext(Context supercontext, Class... serviceClasses) {
		this(supercontext, serviceClassList(serviceClasses));
	}

	/**
	 * Creates a Context that keeps the given services local, while still
	 * providing access to the services of a super context.
	 *
	 * @param supercontext
	 *            the Context that provides access to more services.
	 * @param serviceClasses
	 *            the services this context will provide directly.
	 */
	public SubContext(Context supercontext, Collection<Class<? extends Service>> serviceClasses) {
		this(supercontext, serviceClasses, null);
	}

	/**
	 * Creates a Context that keeps the given services local, while still
	 * providing access to the services of a super context.
	 *
	 * @param supercontext
	 *            the Context that provides access to more services.
	 * @param serviceClasses
	 *            the services this context will provide directly.
	 * @param pluginIndex
	 *            the {@link PluginIndex} to use.
	 */
	public SubContext(Context supercontext, Collection<Class<? extends Service>> serviceClasses,
			PluginIndex pluginIndex) {

		// create fully empty super context
		super(true, true);

		m_superContext = supercontext;
		m_serviceIndex = new ServiceIndex();

		m_pluginIndex = (pluginIndex == null ? new PluginIndex() : pluginIndex);
		m_pluginIndex.discover();

		final ServiceHelper serviceHelper = new ServiceHelper(this, serviceClasses, true);
		serviceHelper.loadServices();

		// enable delegating
		delegating = true;
	}

	@Override
	public ServiceIndex getServiceIndex() {
		if (m_serviceIndex == null) {
			return new ServiceIndex();
		}
		return m_serviceIndex;
	}

	@Override
	public PluginIndex getPluginIndex() {
		if (m_pluginIndex == null) {
			return new PluginIndex();
		}
		return m_pluginIndex;
	}

	@Override
	public boolean isStrict() {
		return true;
	}

	@Override
	public void setStrict(final boolean strict) {
		// NB. No0p
	}

	/**
	 * Gets the service of the given class. Will deliver local Services before
	 * delegate context ones.
	 *
	 * @param c
	 *            the Class of the desired Service.
	 *
	 * @throws NoSuchServiceException
	 *             if the context does not have the requested service.
	 */
	@Override
	public <S extends Service> S service(final Class<S> c) {
		S service = getService(c);
		// service not found
		if (service == null) {
			throw new NoSuchServiceException("Service " + c.getName() + " not found.");
		}
		return service;
	}

	/**
	 * Gets the service of the given class name (useful for scripts).
	 *
	 * @throws IllegalArgumentException
	 *             if the class does not exist, or is not a service class.
	 * @throws NoSuchServiceException
	 *             if the context does not have the requested service.
	 */
	@Override
	public Service service(final String className) {
		final Class<?> c = ClassUtils.loadClass(className);
		if (c == null) {
			throw new IllegalArgumentException("No such class: " + className);
		}
		if (!Service.class.isAssignableFrom(c)) {
			throw new IllegalArgumentException("Not a service class: " + c.getName());
		}
		@SuppressWarnings("unchecked")
		final Class<? extends Service> serviceClass = (Class<? extends Service>) c;
		return service(serviceClass);
	}

	/** Gets the service of the given class name (useful for scripts). */
	@Override
	public Service getService(final String className) {
		final Class<?> c = ClassUtils.loadClass(className);
		if (c == null)
			return null;
		if (!Service.class.isAssignableFrom(c))
			return null; // not a service class
		@SuppressWarnings("unchecked")
		final Class<? extends Service> serviceClass = (Class<? extends Service>) c;
		return getService(serviceClass);
	}

	/**
	 * Gets the service of the given class, or null if there is no matching
	 * service. Will deliver local Services before trying the delegate context.
	 */
	@Override
	public <S extends Service> S getService(final Class<S> c) {
		// This is called from the super constructor
		if (m_serviceIndex == null) {
			return null;
		}
		S service = m_serviceIndex.getService(c);
		// not a local service
		if (service == null && delegating) {
			// try delegate
			service = m_superContext.getService(c);
		}
		return service;
	}
}
