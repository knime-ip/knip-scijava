package org.knime.scijava.commands.util;

import java.util.HashMap;
import java.util.Map;

public final class PrimitiveTypeUtils {
	private static final Map<Class<?>, Class<?>> m_primitvePluginTypes = new HashMap<>();

	static {
		m_primitvePluginTypes.put(byte.class, Byte.class);
		m_primitvePluginTypes.put(short.class, Short.class);
		m_primitvePluginTypes.put(double.class, Double.class);
		m_primitvePluginTypes.put(int.class, Integer.class);
		m_primitvePluginTypes.put(long.class, Long.class);
	}

	private PrimitiveTypeUtils() {
		// NB Utility class
	}

	/**
	 * Checks if the given type is primitive, iff it is it will return the
	 * 
	 * @param type
	 *            the type to check if it is primitive
	 * @return the converted type iff it was primitive, otherwise the input type
	 */
	public static Class<?> convertIfPrimitive(Class<?> type) {
		Class<?> out = m_primitvePluginTypes.get(type);
		return out != null ? out : type;
	}
}
