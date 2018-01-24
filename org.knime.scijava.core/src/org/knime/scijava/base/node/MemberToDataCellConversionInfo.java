package org.knime.scijava.base.node;

import java.util.function.Function;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;

public interface MemberToDataCellConversionInfo<I> extends Function<I, DataCell> {

	String getSourceMemberName();

	DataType getOutputType();

	Function<I, DataCell> getConverter();
}
