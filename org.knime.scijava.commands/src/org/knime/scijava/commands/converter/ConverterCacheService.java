package org.knime.scijava.commands.converter;

import java.util.Optional;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.DataValue;
import org.scijava.service.Service;

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
    DataCell convertToKnime(Object in, Class<?> inType) throws Exception;

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
    Optional<Class<?>> getMatchingJavaType(
            DataType dataType);

}
