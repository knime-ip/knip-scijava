package org.knime.scijava.base.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scijava.struct.Member;
import org.scijava.struct.MemberInstance;
import org.scijava.struct.Struct;
import org.scijava.struct.StructInstance;

public class NodeDialogStructInstance<I> implements StructInstance<I> {

	private Struct m_struct;
	private Map<String, MemberInstance<?>> m_memberInstances;

	public NodeDialogStructInstance(Struct struct) {
		m_struct = struct;
		m_memberInstances = new HashMap<String, MemberInstance<?>>();
		for (Member<?> member : m_struct.members()) {
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
	public I object() {
		// TODO
		throw new UnsupportedOperationException("Not supported.");
	}

	@Override
	public MemberInstance<?> member(String key) {
		return m_memberInstances.get(key);
	}

	class NodeDialogMemberInstance<T> implements MemberInstance<T> {

		private Member<T> m_member;
		private T m_obj;

		NodeDialogMemberInstance(final Member<T> member) {
			m_member = member;
		}

		@Override
		public Member<T> member() {
			return m_member;
		}

		@Override
		public void set(Object value) {
			@SuppressWarnings("unchecked")
			final T t = (T) value;
			m_obj = t;
		}

		@Override
		public T get() {
			return m_obj;
		}
	}

}
