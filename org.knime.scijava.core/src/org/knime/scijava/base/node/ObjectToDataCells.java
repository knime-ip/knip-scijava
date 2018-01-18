package org.knime.scijava.base.node;

import java.util.List;
import java.util.function.Function;

import org.knime.core.data.DataCell;
import org.scijava.ValidityException;
import org.scijava.param.ParameterStructs;
import org.scijava.struct.Struct;
import org.scijava.struct.StructInstance;

public class ObjectToDataCells<I> implements Function<I, DataCell[]> {

	private List<MemberToDataCellConversionInfo<?>> m_infos;
	private Struct m_struct;

	public ObjectToDataCells(Class<I> type, List<MemberToDataCellConversionInfo<?>> infos) throws ValidityException {
		m_infos = infos;
		m_struct = ParameterStructs.structOf(type);
	}

	@Override
	public DataCell[] apply(I in) {
		DataCell[] cells = new DataCell[m_infos.size()];

		// FIXME make a StructInstance updateable to avoid object creation?
		int i = 0;
		for (final MemberToDataCellConversionInfo<?> info : m_infos) {
			cells[++i] = applyCasted(m_struct.createInstance(in), info);
		}
		return cells;
	}

	/**
	 * @param info
	 */
	private <T> DataCell applyCasted(StructInstance<?> inst, MemberToDataCellConversionInfo<T> info) {
		@SuppressWarnings("unchecked")
		T object = (T) inst.member(info.getSourceMemberName()).get();
		return info.getConverter().apply(object);
	}
}
