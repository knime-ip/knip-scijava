package org.knime.scijava.base.node;

import java.util.function.Function;

import org.knime.core.data.DataValue;

public class DefaultDataValueToStructConversionInfo<O> implements DataValueToMemberConversionInfo<O> {

	private Function<DataValue, O> m_conv;
	private String m_memberName;
	private int m_valueIndex;

	public DefaultDataValueToStructConversionInfo(int valueIndex, String memberName, Function<DataValue, O> conv) {
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
