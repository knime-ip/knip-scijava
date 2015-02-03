package org.knime.knip.scijava.core;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import org.eclipse.osgi.internal.baseadaptor.DefaultClassLoader;
import org.eclipse.osgi.internal.loader.BundleLoader;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.osgi.framework.Bundle;

class ResourceAwareClassLoader extends ClassLoader {

	final ArrayList<URL> urls = new ArrayList<URL>();

	public ResourceAwareClassLoader(final DefaultClassLoader contextClassLoader) {
		super(contextClassLoader);

		for (BundleSpecification bundleSpec : ((BundleLoader) contextClassLoader
				.getDelegate()).getBundle().getBundleDescription()
				.getRequiredBundles()) {

			final Bundle bundle = org.eclipse.core.runtime.Platform
					.getBundle(bundleSpec.getName());
			Enumeration<URL> resources;
			try {
				resources = bundle
						.getResources("META-INF/json/org.scijava.plugin.Plugin");
			} catch (IOException e) {
				continue;
			}

			if (resources == null) {
				continue;
			}

			while (resources.hasMoreElements()) {
				final URL resource = resources.nextElement();
				// we want to avoid transitive resolving of dependencies
				final String host = resource.getHost();
				if (bundle.getBundleId() == Long.valueOf(host.substring(0,
						host.indexOf(".")))) {
					urls.add(resource);
				}
			}
		}
	}

	@Override
	public Enumeration<URL> getResources(final String name) throws IOException {
		if (!name.startsWith("META-INF/json")) {
			return Collections.emptyEnumeration();
		}
		urls.addAll(Collections.list(super.getResources(name)));
		return Collections.enumeration(urls);
	}
}