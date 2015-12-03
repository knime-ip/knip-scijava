package org.knime.scijava.commands.adapter;

import org.knime.core.data.DataValue;
import org.scijava.convert.AbstractConverter;

/**
 * Abstract implementation of AbstractConverter as an InputAdapter.
 *
 * @author Jonathan Hale
 *
 * @param <I>
 *            Input {@link DataValue} type
 * @param <O>
 *            Output type
 */
public abstract class AbstractInputAdapter<I extends DataValue, O>
		extends AbstractConverter<I, O> implements InputAdapter<I, O> {

	@Override
	public <T> T convert(final Object arg0, final Class<T> arg1) {
		return (T) getValue((I) arg0);
	}

	protected abstract O getValue(I value);
}
