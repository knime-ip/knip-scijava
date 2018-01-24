package org.knime.scijava.base.node;

import java.util.function.Function;

import org.knime.core.data.DataValue;

public class DefaultDataValueToStructConversionInfo<O> implements DataValueToMemberConversionInfo<O> {

	private final Function<DataValue, O> m_conv;
	private final String m_memberName;
	private final int m_valueIndex;

	public DefaultDataValueToStructConversionInfo(final int valueIndex, final String memberName,
			final Function<DataValue, O> conv) {
		m_valueIndex = valueIndex;
		m_memberName = memberName;
		m_conv = conv;
	}

	@Override
	public String getMemberName() {
		return m_memberName;
	}

	@Override
	public int getColumnIndex() {
		return m_valueIndex;
	}

	@Override
	public Function<DataValue, O> getConverter() {
		return m_conv;
	}

}
