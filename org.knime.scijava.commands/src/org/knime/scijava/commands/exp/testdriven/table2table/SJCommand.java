package org.knime.scijava.commands.exp.testdriven.table2table;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;

// ONE SHOULD BE ABLE TO WRITE COMMANDS LIKE THIS!
public class SJCommand implements Command {

    @Parameter
    private String input;

    @Parameter(type = ItemIO.OUTPUT)
    private long output;

    @Override
    public void run() {
        // bli bla blubb
    }

}
