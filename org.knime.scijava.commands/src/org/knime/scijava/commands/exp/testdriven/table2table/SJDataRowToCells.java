package org.knime.scijava.commands.exp.testdriven.table2table;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.node.ExecutionContext;
import org.knime.scijava.commands.converter.KNIMEConverterService;
import org.scijava.AbstractContextual;
import org.scijava.plugin.Parameter;

public class SJDataRowToCells<IN, OUT> extends AbstractContextual
        implements BiConsumer<DataRow, Consumer<DataCell[]>> {

    @Parameter
    private KNIMEConverterService cs;

    private final SJMap<IN, OUT> mapper;

    private final Map<String, Integer> mapping;

    private final ExecutionContext ctx;

    public SJDataRowToCells(final SJMap<IN, OUT> operator,
            final Map<String, Integer> mapping, final ExecutionContext ctx) {
        this.mapper = operator;
        this.mapping = mapping;
        this.ctx = ctx;
    }

    public void initialize(final ExecutionContext ctx) {
        try {
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void accept(final DataRow in, final Consumer<DataCell[]> consumer) {
        mapper.accept(in, new Consumer<OUT>() {

            @Override
            public void accept(final OUT out) {
                consumer.accept(cells);
            }
        });
    }
}
