package org.knime.scijava.base.node;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.scijava.param2.FunctionalParameterMember;
import org.scijava.struct2.Member;
import org.scijava.struct2.MemberInstance;
import org.scijava.struct2.Struct;

public class NodeDialogStructInstance<C> extends NodeStructInstance<C> {

	public NodeDialogStructInstance(final Struct struct, final C object) {
		super(struct, object);
	}

	@Override
	NodeMemberInstance<?> createMemberInstance(final Member<?> member, final Object c) {
		if (member instanceof FunctionalParameterMember) {
			return new ColumnSelectionMemberInstance<>(member, c);
		} else {
			return new NodeMemberInstance<>(member, c);
		}
	}

	public void update(final DataTableSpec spec) {
		for (final MemberInstance<?> member : members()) {
			if (member instanceof ColumnSelectionMemberInstance) {
				((ColumnSelectionMemberInstance<?>) member).setSpec(spec);
			}
		}
	}

	public static class ColumnSelectionMemberInstance<T> extends NodeMemberInstance<T> {

		private DataTableSpec m_spec;

		private int m_selectedColumnIndex;

		ColumnSelectionMemberInstance(final Member<T> member, final Object c) {
			super(member, c);
		}

		public DataTableSpec getSpec() {
			return m_spec;
		}

		public void setSpec(final DataTableSpec spec) {
			if (m_spec == null || !m_spec.equals(spec)) {
				m_spec = spec;
				fireModelChanged();
			}
		}

		public int getSelectedColumnIndex() {
			return m_selectedColumnIndex;
		}

		public void setSelectedColumnIndex(final int index) {
			m_selectedColumnIndex = index;
		}

		@Override
		public void loadSettingsFrom(final NodeSettingsRO settings, final String key) throws InvalidSettingsException {
			super.loadSettingsFrom(settings, key);
			m_selectedColumnIndex = settings.getInt(key);
		}

		@Override
		public void saveSettingsTo(final NodeSettingsWO settings, final String key) throws InvalidSettingsException {
			super.saveSettingsTo(settings, key);
			settings.addInt(key, m_selectedColumnIndex);
		}
	}
}
