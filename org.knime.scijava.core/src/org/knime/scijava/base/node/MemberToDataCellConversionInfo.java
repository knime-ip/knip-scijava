package org.knime.scijava.base.node;

import java.util.function.Function;

import org.knime.core.data.DataCell;

public interface MemberToDataCellConversionInfo<I> extends Function<I, DataCell> {

	String getSourceMemberName();

	Function<I, DataCell> getConverter();
}
