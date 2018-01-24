package org.knime.scijava.base.node;

import java.util.function.Function;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;

public class DefaultMemberToDataCellConversionInfo<I> implements MemberToDataCellConversionInfo<I> {

	private final String m_memberName;

	final DataType m_outputType;

	private final Function<I, DataCell> m_conv;

	public DefaultMemberToDataCellConversionInfo(final String memberName, final DataType outputType,
			final Function<I, DataCell> conv) {
		m_memberName = memberName;
		m_outputType = outputType;
		m_conv = conv;
	}

	@Override
	public String getMemberName() {
		return m_memberName;
	}

	@Override
	public DataType getOutputType() {
		return m_outputType;
	}

	@Override
	public Function<I, DataCell> getConverter() {
		return m_conv;
	}
}
