package org.knime.scijava.base.node;

import java.util.function.Function;

import org.knime.core.data.DataValue;

public interface DataValueToMemberConversionInfo<O> {

	String getMemberName();

	int getColumnIndex();

	Function<DataValue, O> getConverter();
}
