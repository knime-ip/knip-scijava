package org.knime.scijava.base.node;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.scijava.struct2.Member;
import org.scijava.struct2.MemberInstance;
import org.scijava.struct2.Struct;

public class NodeInputStructInstance<C> extends NodeStructInstance<C> {

	public NodeInputStructInstance(final Struct struct, final C object) {
		super(struct, object);
	}

	@Override
	NodeMemberInstance<?> createMemberInstance(final Member<?> member, final Object c) {
		return new ColumnSelectionMemberInstance<>(member, c);
	}

	private void update(final DataTableSpec spec) {
		for (final MemberInstance<?> member : members()) {
			if (member instanceof ColumnSelectionMemberInstance) {
				((ColumnSelectionMemberInstance<?>) member).setSpec(spec);
			}
		}
	}

	public void loadSettingsFromDialog(final NodeSettingsRO settings, final DataTableSpec spec)
			throws InvalidSettingsException {
		super.loadSettingsFrom(settings);
		update(spec);
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

		// We are in the struct instance of the node model which takes care of
		// the column selection. Hence, we do
		// no actually want?/need to save the value of the backing object but
		// rather the configuration data
		// (column index in this case)
		@Override
		public void loadSettingsFrom(final NodeSettingsRO settings, final String key) throws InvalidSettingsException {
			// super.loadSettingsFrom(settings, key);
			m_selectedColumnIndex = settings.getInt(key);
		}

		@Override
		public void saveSettingsTo(final NodeSettingsWO settings, final String key) throws InvalidSettingsException {
			// super.saveSettingsTo(settings, key);
			settings.addInt(key, m_selectedColumnIndex);
		}
	}
}
