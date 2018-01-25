package org.knime.scijava.base.node;

import java.util.function.Function;

import org.knime.core.data.DataValue;

public class DefaultDataValueToMemberConversionInfo<I extends DataValue, O>
		implements DataValueToMemberConversionInfo<I, O> {

	private final int m_columnIndex;

	private final String m_memberName;

	private final Function<I, O> m_conv;

	public DefaultDataValueToMemberConversionInfo(final int columnindex, final String memberName,
			final Function<I, O> conv) {
		m_columnIndex = columnindex;
		m_memberName = memberName;
		m_conv = conv;
	}

	@Override
	public String getMemberName() {
		return m_memberName;
	}

	@Override
	public int getColumnIndex() {
		return m_columnIndex;
	}

	@Override
	public Function<I, O> getConverter() {
		return m_conv;
	}
}
