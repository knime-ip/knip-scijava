package org.knime.scijava.core;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.internal.preferences.EclipsePreferences;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.equinox.internal.app.EclipseAppContainer;
import org.eclipse.osgi.container.ModuleWire;
import org.eclipse.osgi.container.ModuleWiring;
import org.eclipse.osgi.container.namespaces.EclipsePlatformNamespace;
import org.eclipse.osgi.internal.loader.EquinoxClassLoader;
import org.eclipse.osgi.util.ManifestElement;
import org.knime.core.util.EclipseUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.namespace.BundleNamespace;
import org.osgi.framework.namespace.PackageNamespace;

/**
 * ResourceAwareClassLoader
 * 
 * Class loader which is aware of bundle resources.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Jonathan Hale (University of Konstanz)
 *
 */
@SuppressWarnings("restriction")
public class ResourceAwareClassLoader extends ClassLoader {

	/**
	 * Resources which need to be treated in a special way.
	 */
	// TODO make an extension point to add resources as needed (later)
	private final String[] RESOURCES = new String[] { "META-INF/json/org.scijava.plugin.Plugin",
			"META-INF/services/javax.script.ScriptEngineFactory" };

	private final Map<String, Set<URL>> urls = new HashMap<String, Set<URL>>();
	private final Set<URL> bundleUrls = new HashSet<URL>();

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

		processBuddies(clazz);
		processBundle(FrameworkUtil.getBundle(clazz), true);
	}

	private void processBuddies(final Class<?> clazz) {
		final EquinoxClassLoader loader = (EquinoxClassLoader) clazz.getClassLoader();
		List<ModuleWire> providedWires = loader.getBundleLoader().getWiring().getProvidedModuleWires(null);
		if (providedWires != null) {
			for (ModuleWire wire : providedWires) {
				String namespace = wire.getRequirement().getNamespace();
				if (PackageNamespace.PACKAGE_NAMESPACE.equals(namespace)
						|| BundleNamespace.BUNDLE_NAMESPACE.equals(namespace)) {
					final Bundle child = wire.getRequirerWiring().getBundle();
					final Bundle parent = FrameworkUtil.getBundle(clazz);
					try {
						final ManifestElement[] elements = ManifestElement.parseHeader("Eclipse-RegisterBuddy",
								(String) child.getHeaders().get("Eclipse-RegisterBuddy"));

						if (elements != null) {
							for (ManifestElement element : elements) {
								if (parent.equals(org.eclipse.core.runtime.Platform.getBundle(element.getValue()))) {
									safeAdd(bundleUrls, createBundleURL(child));
									processBundle(child, true);
								}
							}
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	private void processBundle(final Bundle b, final boolean addRessources) {
		final String requireBundle = (String) b.getHeaders().get(Constants.REQUIRE_BUNDLE);
		try {
			final ManifestElement[] elements = ManifestElement.parseHeader(Constants.BUNDLE_CLASSPATH, requireBundle);
			for (final ManifestElement manifestElement : elements) {
				final Bundle bundle = org.eclipse.core.runtime.Platform.getBundle(manifestElement.getValue());

				// get file url for this bundle
				safeAdd(bundleUrls, createBundleURL(bundle));

				if (addRessources) {
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
							// we want to avoid transitive resolving of
							// dependencies
							final String host = resource.getHost();
							if (bundle.getBundleId() == Long.valueOf(host.substring(0, host.indexOf(".")))) {
								safeAdd(urls.get(res), resource);
							}
						}
					}
				}
			}
		} catch (BundleException e) {
			throw new RuntimeException(e);
		}
	}

	private URL createBundleURL(final Bundle bundle) {
		try {
			return new URL("file://" + FileLocator.getBundleFile(bundle).getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Enumeration<URL> getResources(final String name) throws IOException {
		final Set<URL> urlList = urls.get(name);
		if (urlList == null) {
			// nothing special to do here
			return super.getResources(name);
		}

		for (final URL url : Collections.list(super.getResources(name))) {
			safeAdd(urlList, url);
		}

		return Collections.enumeration(urlList);
	}

	/**
	 * Get a set of file URLs to the bundles dependency bundles.
	 * 
	 * @return set of dependency bundle file urls
	 */
	public Set<URL> getBundleUrls() {
		return bundleUrls;
	}

	/*
	 * Add url to urls while making sure, that the resulting file urls are
	 * always unique.
	 * 
	 * @param urls Set to add the url to
	 * 
	 * @param urlToAdd Url to add to the set
	 * 
	 * @see FileLocator
	 */
	private static void safeAdd(final Set<URL> urls, final URL urlToAdd) {
		// make sure the resulting file url is not in urls already
		try {
			final URL fileToAdd = FileLocator.resolve(urlToAdd);

			for (final URL url : urls) {
				if (fileToAdd.equals(FileLocator.resolve(url))) {
					// we found a duplicate, do not add.
					return;
				}
			}
		} catch (IOException e) {
			// ignore
		}

		// no duplicate found, we can safely add this url.
		urls.add(urlToAdd);
	}

}