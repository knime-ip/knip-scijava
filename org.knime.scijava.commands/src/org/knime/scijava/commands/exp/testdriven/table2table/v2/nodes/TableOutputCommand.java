package org.knime.scijava.commands.exp.testdriven.table2table.v2.nodes;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;

public class TableOutputCommand implements Command {

    @Parameter
    private Object someInput;

    @Parameter(style = "ColumnSelection")
    private Object someMoreInput;

    // Isn't too easy to be supported
    @Parameter(type = ItemIO.OUTPUT)
    private Object table;

    @Override
    public void run() {
        // TODO
    }

}
