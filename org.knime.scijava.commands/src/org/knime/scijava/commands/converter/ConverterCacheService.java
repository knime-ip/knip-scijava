package org.knime.scijava.commands.converter;

import org.knime.core.data.DataCell;
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

}