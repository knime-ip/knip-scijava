package org.knime.scijava.commands.exp.testdriven.table2table.v2.nodes;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.knime.scijava.commands.exp.testdriven.table2table.v2.Interfaces.FlatMap;

@Plugin(type  = Op.class, )
public class UnaryFlatMap implements FlatMap<FlatMap, StructOut> {

    @Parameter
    private int extra;

    @Override
    public void flatMap(final StructIn input, final Consumer<StructOut> output) {
        for (int i = 0; i < abc; i++)
            output.accept("" + input);

        BiConsumer<StructIn, Consumer<StructOut>>
    }

    class StructIn{
        String a;
        String b;
    }

    class StructOut{
        String c;
        String d;
    }

}

main{
    ops.run(UnaryFlatMap.class, Consumer<StructOut>, StructIn, extra);
}