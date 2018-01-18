package org.knime.scijava.base.node;

import java.util.List;
import java.util.function.Function;

import org.knime.core.data.DataRow;
import org.scijava.ValidityException;
import org.scijava.param.ParameterStructs;
import org.scijava.struct.StructInstance;

public class DataRowToObject<I> implements Function<DataRow, I> {

	private List<DataValueToMemberConversionInfo<?>> m_infos;
	private StructInstance<I> m_instance;

	public DataRowToObject(Class<I> in, List<DataValueToMemberConversionInfo<?>> infos)
			throws ValidityException, InstantiationException, IllegalAccessException {
		m_infos = infos;
		m_instance = ParameterStructs.create(in.newInstance());
	}

	@Override
	public I apply(DataRow values) {
		for (final DataValueToMemberConversionInfo<?> t : m_infos) {
			// FIXME this can be optimized in terms of runtime (saving info per
			// column and then access the info by index).
			m_instance.member(t.getMemberName()).set(t.getConverter().apply(values.getCell(t.getColumnIndex())));
		}
		return m_instance.object();
	}
}
