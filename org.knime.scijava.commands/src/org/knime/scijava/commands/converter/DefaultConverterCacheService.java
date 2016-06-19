package org.knime.scijava.commands.converter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.DataValue;
import org.knime.core.data.convert.datacell.JavaToDataCellConverter;
import org.knime.core.data.convert.datacell.JavaToDataCellConverterFactory;
import org.knime.core.data.convert.datacell.JavaToDataCellConverterRegistry;
import org.knime.core.data.convert.java.DataCellToJavaConverter;
import org.knime.core.data.convert.java.DataCellToJavaConverterFactory;
import org.knime.core.data.convert.java.DataCellToJavaConverterRegistry;
import org.knime.scijava.commands.KNIMEExecutionService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

/**
 * Caches the converters used in a Node.
 *
 * @author Gabriel Einsdorf (University of Konstanz)
 *
 */
@Plugin(type = ConverterCacheService.class)
public class DefaultConverterCacheService extends AbstractService
        implements ConverterCacheService {

    private final DataCellToJavaConverterRegistry m_inRegister = DataCellToJavaConverterRegistry
            .getInstance();
    private final JavaToDataCellConverterRegistry m_outRegistry = JavaToDataCellConverterRegistry
            .getInstance();

    private final Map<String, DataCellToJavaConverter<?, ?>> m_inConverters = new HashMap<>();
    private final Map<String, JavaToDataCellConverter<?>> m_outConverters = new HashMap<>();

    @Parameter
    private KNIMEExecutionService m_execService;

    @Override
    @SuppressWarnings("unchecked")
    public <O> O convertToJava(final DataCell cell, final Class<O> outputType)
            throws Exception {

        final String key = createInputKey(cell.getType(), outputType);

        final DataCellToJavaConverter<DataCell, ?> converter = (DataCellToJavaConverter<DataCell, ?>) m_inConverters
                .getOrDefault(key,
                        addNewInputConverter(cell.getType(), outputType));
        return (O) converter.convert(cell);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public DataCell convertToKnime(final Object in, final Class<?> inType)
            throws Exception {

        final String key = createOutputKey(inType);

        final JavaToDataCellConverter converter = m_outConverters
                .getOrDefault(key, addNewOutputConverter(inType));
        return converter.convert(in);
    }

    private String createOutputKey(final Class<?> inType) {
        return inType.getName();
    }

    private <I> JavaToDataCellConverter<?> addNewOutputConverter(
            final Class<I> outputType) {

        // TODO Will this use the "Best" dataType?
        final Iterator<JavaToDataCellConverterFactory<?>> factoriesIt = m_outRegistry
                .getFactoriesForSourceType(outputType).iterator();

        if (!factoriesIt.hasNext()) {
            throw new IllegalArgumentException("Can't convert from: "
                    + outputType.getName() + " to a KNIME datatype");
        }

        final JavaToDataCellConverter<?> factory = factoriesIt.next()
                .create(m_execService.getExecutionContext());
        final String key = createOutputKey(outputType);
        m_outConverters.put(key, factory);

        return factory;
    }

    /**
     * Adds the converter to the cache.
     *
     * @param inputType
     * @param outputType
     * @return the added converter
     */
    private <O> DataCellToJavaConverter<?, ?> addNewInputConverter(
            final DataType inputType, final Class<O> outputType) {

        final Optional<DataCellToJavaConverterFactory<Object, O>> factory = m_inRegister
                .getConverterFactory(inputType, outputType);

        if (!factory.isPresent()) {
            throw new IllegalArgumentException("Can't convert from: "
                    + inputType.getName() + " to :" + outputType.getName());
        }
        final DataCellToJavaConverter<Object, O> conv = factory.get().create();
        m_inConverters.put(createInputKey(inputType, outputType), conv);
        return conv;
    }

    private static String createInputKey(final DataType dataType,
            final Class<?> outputType) {
        return dataType.getName() + ":" + outputType.getName();
    }

    @Override
    public Optional<DataType> getConvertedType(final Class<?> type) {

        final Iterator<JavaToDataCellConverterFactory<?>> it = m_outRegistry
                .getFactoriesForSourceType(type).iterator();

        if (it.hasNext()) {
            final JavaToDataCellConverterFactory<?> o = it.next();
            return Optional.of(o.getDestinationType());
        }
        return Optional.empty();
    }

    @Override
    public void flushCaches() {
        m_outConverters.clear();
        m_inConverters.clear();
    }

    @Override
    public Optional<Class<DataValue>> getMatchingInputValueClass(
            final Class<?> type) {
        final Iterator<DataCellToJavaConverterFactory<?, ?>> factories = m_inRegister
                .getFactoriesForDestinationType(type).iterator();

        if (factories.hasNext()) {
            @SuppressWarnings("unchecked")
            final Optional<Class<DataValue>> o = Optional
                    .of(((Class<DataValue>) factories.next().getSourceType()));
            return o;
        }
        return Optional.empty();
    }

    @Override
    public Optional<Class<?>> getMatchingJavaType(final DataType dataType) {

        // FIXME Will this always work with the MissingValue <-> Object
        // Converter?
        final Optional<DataCellToJavaConverterFactory<?, ?>> o = m_inRegister
                .getFactoriesForSourceType(dataType).stream().findFirst();
        if (o.isPresent()) {
            return Optional.of(o.get().getDestinationType());
        }
        return Optional.empty();
    }

}
