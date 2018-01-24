package org.knime.scijava.base.node;

import java.util.function.Function;

import org.knime.core.data.DataValue;

public interface DataValueToMemberConversionInfo<I extends DataValue, O> {

	int getColumnIndex();

	String getMemberName();

	Function<I, O> getConverter();
}
