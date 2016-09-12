package org.knime.scijava.commands.converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.DataValue;
import org.knime.core.data.convert.datacell.JavaToDataCellConverter;
import org.knime.core.data.convert.datacell.JavaToDataCellConverterFactory;
import org.knime.core.data.convert.datacell.JavaToDataCellConverterRegistry;
import org.knime.core.data.convert.java.DataCellToJavaConverter;
import org.knime.core.data.convert.java.DataCellToJavaConverterFactory;
import org.knime.core.data.convert.java.DataCellToJavaConverterRegistry;
import org.knime.core.data.convert.util.ClassUtil;
import org.knime.core.node.ExecutionContext;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

/**
 * Caches the converters used in a Node.
 *
 * @author Gabriel Einsdorf (University of Konstanz)
 */
@Plugin(type = KNIMEConverterService.class)
public class DefaultKNIMEConverterService extends AbstractService
        implements KNIMEConverterService {

    private final DataCellToJavaConverterRegistry m_inRegister = DataCellToJavaConverterRegistry
            .getInstance();
    private final JavaToDataCellConverterRegistry m_outRegistry = JavaToDataCellConverterRegistry
            .getInstance();

    private final Map<String, DataCellToJavaConverter<?, ?>> m_inConverters = new HashMap<>();

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
    @SuppressWarnings("unchecked")
    public DataCell convertToKnime(final Object in, final Class<?> inType,
            final DataType type, final ExecutionContext ctx) throws Exception {

        final JavaToDataCellConverter<Object> converter = (JavaToDataCellConverter<Object>) createNewOutputConverter(
                inType, type, ctx);

        return converter.convert(in);
    }

    /*
     * Create new converter from given java type to output knime DataType.
     */
    private <I> JavaToDataCellConverter<?> createNewOutputConverter(
            final Class<I> type, final DataType knimeType,
            final ExecutionContext ctx) {

        final Class<? extends Object> outputType = ClassUtil
                .ensureObjectType(type);

        final Optional<?> converter = m_outRegistry
                .getPreferredConverterFactory(outputType, knimeType);

        if (converter.isPresent()) {
            return ((JavaToDataCellConverterFactory<?>) converter.get())
                    .create(ctx);
        }

        throw new IllegalArgumentException("Can't convert from: "
                + outputType.getName() + " to a KNIME datatype");
    }

    /**
     * Add the converter to the cache.
     *
     * @param inputType
     * @param outputType
     * @return the added converter
     */
    private <O> DataCellToJavaConverter<?, ?> addNewInputConverter(
            final DataType inputType, final Class<O> outputType) {

        final Optional<DataCellToJavaConverterFactory<DataValue, O>> factory = m_inRegister
                .getPreferredConverterFactory(inputType, outputType);

        if (!factory.isPresent()) {
            throw new IllegalArgumentException("Can't convert from: "
                    + inputType.getName() + " to :" + outputType.getName());
        }
        final DataCellToJavaConverter<DataValue, O> conv = factory.get()
                .create();
        m_inConverters.put(createInputKey(inputType, outputType), conv);
        return conv;
    }

    /*
     * Create a string key for the (knime type, java type) pair
     */
    private static String createInputKey(final DataType dataType,
            final Class<?> outputType) {
        return dataType.getName() + ":" + outputType.getName();
    }

    @Override
    public Optional<DataType> getConvertedType(final Class<?> type) {

        final Class<? extends Object> outputType = ClassUtil
                .ensureObjectType(type);

        final Iterator<JavaToDataCellConverterFactory<?>> it = m_outRegistry
                .getFactoriesForSourceType(outputType).iterator();

        if (it.hasNext()) {
            final JavaToDataCellConverterFactory<?> o = it.next();
            return Optional.of(o.getDestinationType());
        }
        return Optional.empty();
    }

    @Override
    public void flushCaches() {
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
        final Optional<DataCellToJavaConverterFactory<?, ?>> o = m_inRegister
                .getFactoriesForSourceType(dataType).stream().findFirst();
        if (o.isPresent()) {
            return Optional.of(o.get().getDestinationType());
        }
        return Optional.empty();
    }

    @Override
    public Collection<DataCellToJavaConverterFactory<?, ?>> getMatchingJavaTypes(
            final ClassLoader loader, final DataType dataType) {
        final Collection<DataCellToJavaConverterFactory<?, ?>> factories = m_inRegister
                .getFactoriesForSourceType(dataType);
        final Set<DataCellToJavaConverterFactory<?, ?>> validFactories = new HashSet<>();

        for (final DataCellToJavaConverterFactory<?, ?> candidate : factories) {
            if (isOnClassPath(candidate.getDestinationType(), loader)) {
                validFactories.add(candidate);
            }
        }

        return validFactories;
    }

    /*
     * Check whether the given class is on the classpath.
     *
     * @return true if the class is in given loader
     */
    private boolean isOnClassPath(final Class<?> type,
            final ClassLoader loader) {
        try {
            // FIXME: check if there is a more efficient way to do it.
            Class.forName(type.getName(), false, loader);
        } catch (final ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public Collection<JavaToDataCellConverterFactory<?>> getMatchingFactories(
            final Class<?> javaType) {
        return m_outRegistry.getFactoriesForSourceType(
                ClassUtil.ensureObjectType(javaType));
    }

}
