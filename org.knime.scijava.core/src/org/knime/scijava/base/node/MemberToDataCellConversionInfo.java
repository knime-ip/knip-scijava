package org.knime.scijava.base.node;

import java.util.function.Function;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;

public interface MemberToDataCellConversionInfo<I> {

	String getMemberName();

	// TODO: Add 'int getColumnIndex()' (see input conversion info)? Might be useful for column rearranging.

	DataType getOutputType();

	Function<I, DataCell> getConverter();
}
