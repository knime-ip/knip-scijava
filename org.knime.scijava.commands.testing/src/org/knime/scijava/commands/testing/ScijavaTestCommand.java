package org.knime.scijava.commands.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.knime.scijava.commands.StyleHook;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;

/**
 * Test command which verifies that inputs have been filled by the preprocessor
 * and feeds them directly back in the outputs which can then be collected into
 * a data row.
 *
 * @author Jonathan Hale
 */
public class ScijavaTestCommand implements Command {

	@Parameter(type = ItemIO.INPUT)
	Boolean emptyTest;

	@Parameter(type = ItemIO.INPUT, style = StyleHook.COLUMNSELECTION)
	Boolean b;
	@Parameter(type = ItemIO.INPUT, style = StyleHook.COLUMNSELECTION)
	Integer i;
	@Parameter(type = ItemIO.INPUT, style = StyleHook.COLUMNSELECTION)
	Long l;
	@Parameter(type = ItemIO.INPUT, style = StyleHook.COLUMNSELECTION)
	String str;

	@Parameter(type = ItemIO.OUTPUT)
	Boolean ob;
	@Parameter(type = ItemIO.OUTPUT)
	Integer oi;
	@Parameter(type = ItemIO.OUTPUT)
	Long ol;
	@Parameter(type = ItemIO.OUTPUT)
	String ostr;

	@Override
	public void run() {
		if (!emptyTest) {
			assertTrue("Boolean input was not filled correctly!", b);
			assertEquals("Integer input was not filled correctly!", new Integer(42000), i);
			assertEquals("Long input was not filled correctly!", new Long(4200000), l);
			assertEquals("String input was not filled correctly!", "KNIME", str);
		} else {
			assertEquals("Integer input was not filled correctly!", null, b);
			assertEquals("Integer input was not filled correctly!", null, i);
			assertEquals("Long input was not filled correctly!", null, l);
			assertEquals("String input was not filled correctly!", null, str);
		}

		ob = b;
		oi = i;
		ol = l;
		ostr = str;
	}

}