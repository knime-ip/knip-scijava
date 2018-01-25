package org.knime.scijava.base.node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.scijava.struct2.Member;
import org.scijava.struct2.MemberInstance;
import org.scijava.struct2.Struct;
import org.scijava.struct2.StructInstance;

public abstract class NodeStructInstance<C> implements StructInstance<C> {

	private final Struct m_struct;

	private final C m_object;

	private final LinkedHashMap<String, NodeMemberInstance<?>> m_memberInstances;

	public NodeStructInstance(final Struct struct, final C object) {
		m_struct = struct;
		m_object = object;
		m_memberInstances = new LinkedHashMap<>();
		for (final Member<?> member : m_struct.members()) {
			m_memberInstances.put(member.getKey(), createMemberInstance(member));
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

	abstract NodeMemberInstance<?> createMemberInstance(Member<?> member);

	public void saveSettingsTo(final NodeSettingsWO settings) throws InvalidSettingsException {
		for (final Entry<String, NodeMemberInstance<?>> entry : m_memberInstances.entrySet()) {
			final String key = entry.getKey();
			final NodeMemberInstance<?> value = entry.getValue();
			value.saveSettingsTo(settings, key);
		}
	}

	public void loadSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		for (final Entry<String, NodeMemberInstance<?>> entry : m_memberInstances.entrySet()) {
			final String key = entry.getKey();
			final NodeMemberInstance<?> value = entry.getValue();
			value.loadSettingsFrom(settings, key);
		}
	}
}
