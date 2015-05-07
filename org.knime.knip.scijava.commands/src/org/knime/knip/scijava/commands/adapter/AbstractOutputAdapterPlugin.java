package org.knime.knip.scijava.commands.adapter;

import org.knime.core.data.DataCell;
import org.scijava.convert.AbstractConverter;

/**
 * Abstract implementation of some basic behavior of OutputAdapterPlugins.
 * @author Jonathan Hale (University of Konstanz)
 *
 * @param <I>
 * @param <D>
 */
public abstract class AbstractOutputAdapterPlugin<I, D extends DataCell> extends AbstractConverter<I, D> implements OutputAdapterPlugin<I, D> {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(Object src, Class<T> dest) {
		
		if (!dest.isAssignableFrom(getOutputType())) {
			return null;
		}
		
		if (getInputType().isAssignableFrom(src.getClass())) {
			return (T) createCell((I) src);
		}
		
		return null;
	}

}
