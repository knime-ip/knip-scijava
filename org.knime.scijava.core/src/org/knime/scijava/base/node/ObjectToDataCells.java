package org.knime.scijava.base.node;

import java.util.List;
import java.util.function.Function;

import org.knime.core.data.DataCell;
import org.scijava.param2.ParameterStructs;
import org.scijava.param2.ValidityException;
import org.scijava.struct2.Struct;
import org.scijava.struct2.StructInstance;

public class ObjectToDataCells<I> implements Function<I, DataCell[]> {

	private final List<MemberToDataCellConversionInfo<?>> m_infos;

	private final Struct m_inStruct;

	public ObjectToDataCells(final Class<I> type, final List<MemberToDataCellConversionInfo<?>> infos)
			throws ValidityException {
		m_infos = infos;
		m_inStruct = ParameterStructs.structOf(type);
	}

	@Override
	public DataCell[] apply(final I in) {
		final DataCell[] cells = new DataCell[m_infos.size()];
		// TODO: Make a StructInstance updateable to avoid object creation?
		for (int i = 0; i < m_infos.size(); i++) {
			cells[i] = applyCasted(m_inStruct.createInstance(in), m_infos.get(i));
		}
		return cells;
	}

	private <T> DataCell applyCasted(final StructInstance<?> inInstance, final MemberToDataCellConversionInfo<T> info) {
		@SuppressWarnings("unchecked")
		final T object = (T) inInstance.member(info.getMemberName()).get();
		return info.getConverter().apply(object);
	}
}
