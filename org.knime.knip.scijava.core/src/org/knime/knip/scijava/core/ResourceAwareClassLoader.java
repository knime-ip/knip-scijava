package org.knime.knip.scijava.core;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;

public class ResourceAwareClassLoader extends ClassLoader {

	final ArrayList<URL> urls = new ArrayList<URL>();

	public ResourceAwareClassLoader(final ClassLoader cl) {
		super(cl);

		final String requireBundle = (String) FrameworkUtil.getBundle(getClass())
				.getHeaders().get(Constants.REQUIRE_BUNDLE);
		try {
			final ManifestElement[] elements = ManifestElement.parseHeader(
					Constants.BUNDLE_CLASSPATH, requireBundle);
			for (final ManifestElement manifestElement : elements) {
				final Bundle bundle = org.eclipse.core.runtime.Platform
						.getBundle(manifestElement.getValue());

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
		} catch (BundleException e) {
			e.printStackTrace();
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