package org.knime.scijava.commands.exp.testdriven.generic;

import org.knime.base.node.mine.pca.PCAModelPortObject;
import org.knime.base.node.mine.pca.PCAModelPortObjectSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.scijava.commands.exp.node.SJFunctionNode;
import org.scijava.plugin.AbstractRichPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

// THIS IS THE DEFINITION OF A FULL FLEDGED KNIME NODES BASED ON SCIJAVA
@Plugin(type = SJFunctionNode.class)
public class OtherPortsTest extends AbstractRichPlugin implements
        SJFunctionNode<PCAModelPortObjectSpec, DataTableSpec, PCAModelPortObject, BufferedDataTable> {

    @Parameter
    private double someParam;

    // @Parameter
    // private ColumnSelection colSel;

    @Override
    public BufferedDataTable execute(final ExecutionContext ctx,
            final PCAModelPortObject t) {

        // TODO
        final BufferedDataContainer outContainer = ctx
                .createDataContainer(null);

        outContainer.close();
        return outContainer.getTable();
    }

    @Override
    public DataTableSpec configure(final PCAModelPortObjectSpec sj) {
        return null;
    }

}
