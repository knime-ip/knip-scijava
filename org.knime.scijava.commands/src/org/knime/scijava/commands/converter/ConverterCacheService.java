package org.knime.scijava.commands.converter;

import java.util.Collection;
import java.util.Optional;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.DataValue;
import org.knime.core.data.convert.datacell.JavaToDataCellConverterFactory;
import org.knime.core.data.convert.java.DataCellToJavaConverterFactory;
import org.knime.core.node.ExecutionContext;
import org.scijava.service.Service;

// FIXME NAMING (KNIPConverterCache?!)
// FIXME CACHING (isn't implemented everywhere, yet)
// FIXME can we always delegate to Jonathans implementations?
public interface ConverterCacheService extends Service {

    /**
     * Convert a KNIME cell to the fitting java type.
     *
     * @param <O>
     *            the output Type
     * @param cell
     *            the KNIME cell
     * @param outputType
     *            the desired output type
     * @return The converted object
     * @throws Exception
     *             When the conversion fails
     */
    <O> O convertToJava(DataCell cell, Class<O> outputType) throws Exception;

    /**
     * Convert a java object to the fitting KNIME cell.
     *
     * @param in
     *            The object to be converted
     * @param inType
     *            the type of the object
     * @return A DataCell with the converted value
     * @throws Exception
     *             When the conversion fails
     */
    DataCell convertToKnime(Object in, Class<?> inType, DataType type,
            ExecutionContext es) throws Exception;

    /**
     * Flush all caches.
     */
    void flushCaches();

    /**
     * Gets the converted type.
     *
     * @param type
     *            the type
     * @return the converted type
     */
    Optional<DataType> getConvertedType(Class<?> type);

    /**
     * Gets the matching input value class.
     *
     * @param type
     *            the type
     * @return the matching input value class
     */
    Optional<Class<DataValue>> getMatchingInputValueClass(Class<?> type);

    /**
     * Gets the matching java type for a given data value.
     *
     * @param dataType
     *            the preferred value class
     * @return the matching java type
     */
    Optional<Class<?>> getMatchingJavaType(DataType dataType);

    /**
     * @FIXME
     *
     * @return
     */
    Collection<DataCellToJavaConverterFactory<?, ?>> getMatchingJavaTypes(
            ClassLoader loader, DataType dataType);

    /**
     * FIXME
     *
     * @return
     */
    Collection<JavaToDataCellConverterFactory<?>> getMatchingFactories(
            Class<?> javaType);

}
