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
	private final Struct m_struct;

	public ObjectToDataCells(final Class<I> type, final List<MemberToDataCellConversionInfo<?>> infos)
			throws ValidityException {
		m_infos = infos;
		m_struct = ParameterStructs.structOf(type);
	}

	@Override
	public DataCell[] apply(final I in) {
		final DataCell[] cells = new DataCell[m_infos.size()];

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
	private <T> DataCell applyCasted(final StructInstance<?> inst, final MemberToDataCellConversionInfo<T> info) {
		@SuppressWarnings("unchecked")
		final T object = (T) inst.member(info.getSourceMemberName()).get();
		return info.getConverter().apply(object);
	}
}
