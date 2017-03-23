package org.knime.scijava.commands.exp.node;

import java.util.function.Consumer;

import org.knime.core.data.DataCell;
import org.scijava.convert.Converter;

public class SJConsumer<T> implements Consumer<T> {

    private final Consumer<DataCell[]> consumer;
    private final Converter<T, DataCell[]> converter;

    public SJConsumer(final Consumer<DataCell[]> consumer,
            final Converter<T, DataCell[]> converter) {
        this.consumer = consumer;
        this.converter = converter;
    }

    @Override
    public void accept(final T t) {
        consumer.accept(converter.convert(consumer, DataCell[].class));
    }

}
