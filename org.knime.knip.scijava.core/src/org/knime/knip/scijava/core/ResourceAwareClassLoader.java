package org.knime.knip.scijava.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;

/**
 * ResourceAwareClassLoader
 * 
 * Class loader which is aware of bundle resources.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Jonathan Hale (University of Konstanz)
 *
 */
public class ResourceAwareClassLoader extends ClassLoader {

	final ArrayList<URL> urls = new ArrayList<URL>();
	final ArrayList<URL> fileUrls = new ArrayList<URL>();

	/**
	 * Constructor.
	 * 
	 * Parses current bundle resources of required bundles of
	 * org.knime.knip.scijava.core and caches their urls.
	 * 
	 * @param parent
	 *            The parent class loader
	 * @deprecated use {@link #ResourceAwareClassLoader(ClassLoader, Class)}
	 *             instead.
	 */
	public ResourceAwareClassLoader(final ClassLoader parent) {
		this(parent, null);
	}

	/**
	 * Constructor.
	 * 
	 * Parses current bundle resources of required bundles of
	 * c's bundle and caches their urls.
	 * 
	 * @param parent
	 *            The parent class loader
	 * @param clazz
	 *            Class whose bundles requirements will be parsed
	 */
	public ResourceAwareClassLoader(final ClassLoader parent, Class<?> clazz) {
		super(parent);

		if (clazz == null) {
			clazz = getClass();
		}

		final String requireBundle = (String) FrameworkUtil.getBundle(clazz)
				.getHeaders().get(Constants.REQUIRE_BUNDLE);
		try {
			final ManifestElement[] elements = ManifestElement.parseHeader(
					Constants.BUNDLE_CLASSPATH, requireBundle);
			for (final ManifestElement manifestElement : elements) {
				final Bundle bundle = org.eclipse.core.runtime.Platform
						.getBundle(manifestElement.getValue());

				try {
					// get file url for this bundle
					fileUrls.add(new URL("file://"
							+ FileLocator.getBundleFile(bundle)
									.getAbsolutePath()));
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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

	/**
	 * @return urls to the eclipse resources on the classpath. Most commonly the
	 *         urls are defined using bundleresource protocol.
	 */
	public Collection<URL> getURLs() {
		return urls;
	}

	/**
	 * @return urls to the eclipse resources on the classpath defined using the
	 *         file protocol.
	 */
	public Collection<URL> getFileURLs() {
		return fileUrls;
	}
}