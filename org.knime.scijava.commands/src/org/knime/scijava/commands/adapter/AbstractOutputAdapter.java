package org.knime.scijava.commands.adapter;

import org.knime.core.data.DataValue;
import org.scijava.convert.AbstractConverter;

/**
 * Abstract implementation of some basic behavior of OutputAdapterPlugins.
 *
 * @author Jonathan Hale (University of Konstanz)
 * @author Christian Dietz, University of Konstanz<
 *
 * @param <I>
 * @param <D>
 */
public abstract class AbstractOutputAdapter<I, D extends DataValue>
		extends AbstractConverter<I, D>implements OutputAdapter<I, D> {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(final Object src, final Class<T> dest) {

		if (src == null || dest == null) {
			return null;
		}

		if (!dest.isAssignableFrom(getOutputType())) {
			return null;
		}

		if (getInputType().isAssignableFrom(src.getClass())) {
			return (T) createCell((I) src);
		}

		return null;
	}

	/**
	 * Create a suitable cell given the input
	 *
	 * @param obj
	 *            object of type i
	 * @return DataCell wrapping the incoming obj of type I
	 */
	protected abstract D createCell(final I obj);

}
