package org.knime.scijava.base.node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.ClassUtils;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.scijava.param2.ParameterMember;
import org.scijava.struct2.Member;
import org.scijava.struct2.MemberInstance;
import org.scijava.struct2.Struct;
import org.scijava.struct2.StructInstance;

public class NodeStructInstance<C> implements StructInstance<C> {

	private final Struct m_struct;

	private final C m_object;

	private final LinkedHashMap<String, NodeDialogMemberInstance<?>> m_memberInstances;

	public NodeStructInstance(final Struct struct, final C object) {
		m_struct = struct;
		m_object = object;
		m_memberInstances = new LinkedHashMap<>();
		for (final Member<?> member : m_struct.members()) {
			m_memberInstances.put(member.getKey(), new NodeDialogMemberInstance<>(member));
		}
	}

	@Override
	public List<MemberInstance<?>> members() {
		return new ArrayList<>(m_memberInstances.values());
	}

	@Override
	public Struct struct() {
		return m_struct;
	}

	@Override
	public C object() {
		return m_object;
	}

	@Override
	public MemberInstance<?> member(final String key) {
		return m_memberInstances.get(key);
	}

	public void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
		for (final Entry<String, NodeDialogMemberInstance<?>> entry : m_memberInstances.entrySet()) {
			final String key = entry.getKey();
			final NodeDialogMemberInstance<?> value = entry.getValue();
			value.saveSettingsTo(settings, key);
		}
	}

	public void loadSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		for (final Entry<String, NodeDialogMemberInstance<?>> entry : m_memberInstances.entrySet()) {
			final String key = entry.getKey();
			final NodeDialogMemberInstance<?> value = entry.getValue();
			value.loadSettingsFrom(settings, key);
		}
	}

	public static class NodeDialogMemberInstance<T> implements MemberInstance<T> {

		private final Member<T> m_member;

		private final boolean m_persist;

		private T m_obj;

		private final CopyOnWriteArrayList<BiConsumer<MemberInstance<T>, T>> m_changeListeners = new CopyOnWriteArrayList<>();

		NodeDialogMemberInstance(final Member<T> member) {
			m_member = member;
			m_persist = member instanceof ParameterMember && ((ParameterMember<?>) member).isPersisted();
		}

		@Override
		public Member<T> member() {
			return m_member;
		}

		@Override
		public void set(final Object value) {
			final T oldValue = m_obj;
			@SuppressWarnings("unchecked")
			final T newValue = (T) value;
			m_obj = newValue;
			if (!Objects.equals(oldValue, newValue)) {
				onChanged(oldValue);
			}
		}

		@Override
		public T get() {
			return m_obj;
		}

		public void saveSettingsTo(final NodeSettingsWO settings, final String key) throws InvalidSettingsException {
			if (m_persist) {
				final Object value = m_obj;
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

		private void onChanged(final T oldValue) {
			for (final BiConsumer<MemberInstance<T>, T> listener : m_changeListeners) {
				listener.accept(this, oldValue);
			}
		}
	}
}
