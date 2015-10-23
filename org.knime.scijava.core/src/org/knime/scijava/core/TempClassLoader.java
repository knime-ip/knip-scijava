package org.knime.scijava.core;

/**
 * Class to temporarily set the context class loader of the current
 * {@link Thread} in a try-catch scope for example:
 * 
 * <pre>
 * <code>
 * ClassLoader cl; // ClassLoader to use for try-catch block.
 * 
 * try (TempClassLoader tl = new TempClassLoader(cl)) {
 * 		// cl is used
 * } finally {
 * }
 * 
 * // ClassLoader has been reset to what it was before try block.
 * }
 * </code>
 * </pre>
 * 
 * Or in non try-catch use cases:
 * 
 * <pre>
 * <code>
 * ClassLoader cl; // ClassLoader to use in the following scope
 * TempClassLoader tl = new TempClassLoader(cl);
 * 
 * // cl is used
 * 
 * tl.close(); // ClassLoader is reset.
 * </code>
 * </pre>
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
public class TempClassLoader implements AutoCloseable {

	/* Backed up ClassLoader to restore after close() */
	private final ClassLoader m_previousClassLoader;

	/**
	 * Constructor
	 * @param cl ClassLoader to use until {@link #close()} is called.
	 */
	public TempClassLoader(ClassLoader cl) {
		m_previousClassLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(cl);
	}

	@Override
	public void close() {
		// reset the class loader
		Thread.currentThread().setContextClassLoader(m_previousClassLoader);
	}

}
