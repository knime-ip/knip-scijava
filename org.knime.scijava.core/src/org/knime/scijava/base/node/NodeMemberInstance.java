package org.knime.scijava.base.node;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.ClassUtils;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.scijava.param2.ParameterMember;
import org.scijava.struct2.Member;
import org.scijava.struct2.MemberInstance;
import org.scijava.struct2.ValueAccessible;

//TODO: this class partly mirrors ValueAccessibleMemberInstance, inherit?
public class NodeMemberInstance<T> implements MemberInstance<T> {

	private final Member<T> m_member;

	private final ValueAccessible<T> m_access;

	private final Object m_object;

	private final boolean m_persist;

	private final CopyOnWriteArrayList<BiConsumer<MemberInstance<T>, T>> m_changeListeners = new CopyOnWriteArrayList<>();

	NodeMemberInstance(final Member<T> member, final Object object) {
		m_member = member;
		// FIXME: m_access and supporting get and set based on it being
		// non-null does not make sense design-wise
		// TODO: also, type-safety
		m_access = member instanceof ValueAccessible ? ((ValueAccessible<T>) member) : null;
		m_object = object;
		m_persist = member instanceof ParameterMember && ((ParameterMember<?>) member).isPersisted();
	}

	@Override
	public Member<T> member() {
		return m_member;
	}

	@Override
	public T get() {
		if (m_access == null) {
			throw new UnsupportedOperationException("implementation to be changed");
		}
		return m_access.get(m_object);
	}

	@Override
	public void set(final Object value) {
		// FIXME call changed if value changes
		if (m_access == null) {
			throw new UnsupportedOperationException("implementation to be changed");
		}
		// TODO: type-safety, see ValueAccessibleMemberInstance#set(Object)
		final T tValue = (T) value;
		m_access.set(tValue, m_object);
	}

	public void saveSettingsTo(final NodeSettingsWO settings, final String key) throws InvalidSettingsException {
		if (m_persist) {
			final Object value = get();
			Class<?> valueType = m_member.getRawType();
			if (valueType.isPrimitive()) {
				valueType = ClassUtils.primitiveToWrapper(valueType);
			}
			if (Boolean.class.equals(valueType)) {
				settings.addBoolean(key, value != null ? (Boolean) value : false);
			} else if (Double.class.equals(valueType)) {
				settings.addDouble(key, value != null ? (Double) value : 0.0);
			} else if (Integer.class.equals(valueType)) {
				settings.addInt(key, value != null ? (Integer) value : 0);
			} else if (Long.class.equals(valueType)) {
				settings.addLong(key, value != null ? (Long) value : 0);
			} else if (String.class.equals(valueType)) {
				settings.addString(key, value != null ? (String) value : "");
			} else if (String[].class.equals(valueType)) {
				settings.addStringArray(key, value != null ? (String[]) value : new String[0]);
			} else {
				throw new UnsupportedOperationException("Cannot save member instance '" + key
						+ "' to KNIME settings. Value type " + valueType + " is not supported.");
			}
		}
	}

	public void loadSettingsFrom(final NodeSettingsRO settings, final String key) throws InvalidSettingsException {
		if (m_persist) {
			Object value;
			Class<?> valueType = m_member.getRawType();
			if (valueType.isPrimitive()) {
				valueType = ClassUtils.primitiveToWrapper(valueType);
			}
			if (Boolean.class.equals(valueType)) {
				value = new Boolean(settings.getBoolean(key));
			} else if (Double.class.equals(valueType)) {
				value = new Double(settings.getDouble(key));
			} else if (Integer.class.equals(valueType)) {
				value = new Integer(settings.getInt(key));
			} else if (Long.class.equals(valueType)) {
				value = new Long(settings.getLong(key));
			} else if (String.class.equals(valueType)) {
				value = settings.getString(key);
			} else if (String[].class.equals(valueType)) {
				value = settings.getStringArray(key);
			} else {
				throw new UnsupportedOperationException("Cannot load member instance '" + key
						+ "' from KNIME settings. Value type " + valueType + " is not supported.");
			}
			set(value);
		}
	}

	@Override
	public void addChangeListener(final BiConsumer<MemberInstance<T>, T> listener) {
		if (!m_changeListeners.contains(listener)) {
			m_changeListeners.add(listener);
		}
	}

	@Override
	public void removeChangeListener(final BiConsumer<MemberInstance<T>, T> listener) {
		m_changeListeners.remove(listener);
	}

	public void fireModelChanged() {
		// FIXME call with real old value
		onChanged(null);
	}

	private void onChanged(final T oldValue) {
		for (final BiConsumer<MemberInstance<T>, T> listener : m_changeListeners) {
			listener.accept(this, oldValue);
		}
	}
}