package org.knime.scijava.base.node;

import java.util.List;
import java.util.function.Function;

import org.knime.core.data.DataRow;
import org.scijava.param2.ParameterStructs;
import org.scijava.param2.ValidityException;
import org.scijava.struct2.StructInstance;

public class DataRowToObject<O> implements Function<DataRow, O> {

	private final List<DataValueToMemberConversionInfo<?, ?>> m_infos;

	private final StructInstance<O> m_outInstance;

	public DataRowToObject(final Class<O> out, final List<DataValueToMemberConversionInfo<?, ?>> infos)
			throws ValidityException, InstantiationException, IllegalAccessException {
		m_infos = infos;
		m_outInstance = ParameterStructs.create(out.newInstance());
	}

	@Override
	public O apply(final DataRow values) {
		for (final DataValueToMemberConversionInfo<?, ?> t : m_infos) {
			// TODO: This can be optimized in terms of runtime (saving info per column and then access the info by
			// index).
			m_outInstance.member(t.getMemberName()).set(t.getConverter().apply(values.getCell(t.getColumnIndex())));
		}
		return m_outInstance.object();
	}
}
