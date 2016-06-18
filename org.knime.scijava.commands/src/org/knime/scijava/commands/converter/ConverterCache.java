package org.knime.scijava.commands.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.convert.java.DataCellToJavaConverter;
import org.knime.core.data.convert.java.DataCellToJavaConverterFactory;
import org.knime.core.data.convert.java.DataCellToJavaConverterRegistry;

public class ConverterCache {

	DataCellToJavaConverterRegistry m_register = DataCellToJavaConverterRegistry
			.getInstance();

	Map<String, DataCellToJavaConverter<?, ?>> m_converters = new HashMap<>();

	static ConverterCache m_instance;

	private ConverterCache() {

	}

	public static ConverterCache getInstance() {

		if (m_instance == null) {
			m_instance = new ConverterCache();
		}
		return m_instance;
	}

	public <O> O convert(DataCell cell, Class<O> outputType) throws Exception {

		String key = createKey(cell.getType(), outputType);

		DataCellToJavaConverter<DataCell, ?> converter = (DataCellToJavaConverter<DataCell, ?>) m_converters
				.getOrDefault(key, addNewConverter(cell.getType(), outputType));
		return (O) converter.convert(cell);
	}


	/**
	 * Adds the converter to the cache
	 * 
	 * @param inputType
	 * @param outputType
	 * @return the added converter
	 */
	private <O> DataCellToJavaConverter<?, ?> addNewConverter(
			DataType inputType, Class<O> outputType) {

		Optional<DataCellToJavaConverterFactory<Object, O>> factory = m_register
				.getConverterFactory(inputType, outputType);

		if (!factory.isPresent()) {
			throw new IllegalArgumentException("Can't convert from: "
					+ inputType.getName() + " to :" + outputType.getName());
		}
		DataCellToJavaConverter<Object, O> conv = factory.get().create();
		m_converters.put(createKey(inputType, outputType), conv);
		return conv;
	}

	private static String createKey(DataType dataType, Class<?> outputType) {
		return dataType.getName() + ":" + outputType.getName();
	}
}
