package org.knime.scijava.commands.adapter;

import java.util.HashMap;
import java.util.Map;

import org.knime.scijava.commands.util.PrimitiveTypeUtils;
import org.scijava.convert.ConvertService;
import org.scijava.plugin.AbstractSingletonService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of OutputAdapterService.
 *
 * @author Jonathan Hale (University of Konstanz)
 * @author Gabriel Einsdorf (University of Konstanz)
 *
 */
@SuppressWarnings("rawtypes")
@Plugin(type = OutputAdapterService.class)
public class DefaultOutputAdapterService extends
		AbstractSingletonService<OutputAdapter>implements OutputAdapterService {

	private final Map<Class<?>, OutputAdapter> m_adapterByValueClass = new HashMap<>();

	@Parameter
	private ConvertService cs;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<OutputAdapter> getPluginType() {
		return OutputAdapter.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public OutputAdapter getMatchingOutputAdapter(final Class<?> valueClass) {

		// check adapter cache
		final OutputAdapter adapter = m_adapterByValueClass.get(valueClass);
		if (adapter != null) {
			return adapter;
		}

		// check primitive conversion cache
		Class<?> checkValue = PrimitiveTypeUtils.convertIfPrimitive(valueClass);

		// search for output adapter
		for (final OutputAdapter outputAdapter : this.getInstances()) {
			if (outputAdapter.getInputType().isAssignableFrom(checkValue)) {
				m_adapterByValueClass.put(valueClass, outputAdapter);
				return outputAdapter;
			}
		}

		return null;
	}

}
