package org.knime.knip.scijava.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

	/**
	 * Resources which need to be treated in a special way.
	 */
	// TODO make an extension point to add resources as needed (later)
	private final String[] RESOURCES = new String[] {
			"META-INF/json/org.scijava.plugin.Plugin",
			"META-INF/services/javax.script.ScriptEngineFactory" };

	private final Map<String, Set<URL>> urls = new HashMap<String, Set<URL>>();
	private final ArrayList<URL> fileUrls = new ArrayList<URL>();

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
	 * Parses current bundle resources of required bundles of c's bundle and
	 * caches their urls.
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

		// initialize urls map
		for (final String res : RESOURCES) {
			urls.put(res, new HashSet<URL>());
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

				for (final String res : RESOURCES) {
					Enumeration<URL> resources;
					try {
						resources = bundle.getResources(res);
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
						if (bundle.getBundleId() == Long.valueOf(host
								.substring(0, host.indexOf(".")))) {
							urls.get(res).add(resource);
						}
					}
				}
			}
		} catch (BundleException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Enumeration<URL> getResources(final String name) throws IOException {
		final Set<URL> urlList = urls.get(name);
		if (urlList == null) {
			// nothing special to do here
			return super.getResources(name);
		}
		urlList.addAll(Collections.list(super.getResources(name)));
		return Collections.enumeration(urlList);
	}
}