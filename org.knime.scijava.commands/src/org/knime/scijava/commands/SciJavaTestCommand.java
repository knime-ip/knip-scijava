
package org.knime.scijava.commands;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 *
 * Example Command to be wrapped as KNIME Node.
 *
 * @author Christian Dietz, University of Konstanz
 */
@Plugin(type = Command.class, headless = true, label = "Test Command")
public class SciJavaTestCommand implements Command {
    @Parameter
    private MultiOutputListener rowOutput;

    @Parameter(label = "From Text", style = StyleHook.COLUMNSELECTION)
    private String fromText;

    @Parameter(label = "From Int")
    private int fromInt;

    @Parameter(type = ItemIO.OUTPUT, label = "Output Int")
    private int outInt;

    @Override
    public void run() {
        try {
            for (int i = 0; i < fromInt; i++) {
                outInt = i;
                rowOutput.notifyListener();
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
