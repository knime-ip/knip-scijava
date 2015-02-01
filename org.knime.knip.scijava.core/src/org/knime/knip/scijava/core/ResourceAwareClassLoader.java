package org.knime.knip.scijava.core;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import org.eclipse.osgi.internal.baseadaptor.DefaultClassLoader;
import org.eclipse.osgi.internal.loader.BundleLoader;
import org.eclipse.osgi.service.resolver.BundleSpecification;

/**
 * TODO: Avoid duplicates
 */
@SuppressWarnings("restriction")
public class ResourceAwareClassLoader extends ClassLoader {

	final ArrayList<URL> urls = new ArrayList<URL>();

	public ResourceAwareClassLoader(final DefaultClassLoader contextClassLoader) {
		super(contextClassLoader);

		for (BundleSpecification bundle : ((BundleLoader) contextClassLoader
				.getDelegate()).getBundle().getBundleDescription()
				.getRequiredBundles()) {

			URL resource = org.eclipse.core.runtime.Platform.getBundle(
					bundle.getName()).getResource(
					"META-INF/json/org.scijava.plugin.Plugin");

			if (resource != null) {
				urls.add(resource);
				System.out.println(bundle.getName());
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