package org.knime.scijava.commands.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.LongCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.NodeLogger;
import org.knime.scijava.commands.CellOutput;
import org.knime.scijava.commands.SciJavaGateway;
import org.knime.scijava.commands.module.NodeModule;
import org.knime.scijava.commands.module.NodeModuleService;
import org.scijava.Context;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.command.CommandInfo;
import org.scijava.command.CommandService;
import org.scijava.plugin.Parameter;

/**
 * Test for {@link ColumnInputMappingKnimePreprocessor} and
 * {@link DefaultKnimePostprocessor}.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
public class NodeModuleTest {

	private static Context context;

	@Parameter
	CommandService m_commandService;

	@Parameter
	NodeModuleService m_nodeModuleService;

	// Create the test table
	private static final DataRow m_testRow = new DefaultRow(new RowKey("TestRow001"), BooleanCell.TRUE, new IntCell(42),
			new IntCell(420), new IntCell(42000), new LongCell(4200000), new StringCell("KNIME"), new StringCell(" "));

	private static final DataTableSpec m_spec = new DataTableSpec(new String[] { "b", "by", "s", "i", "l", "str", "c" },
			new DataType[] { BooleanCell.TYPE, IntCell.TYPE, IntCell.TYPE, IntCell.TYPE, LongCell.TYPE, StringCell.TYPE,
					StringCell.TYPE });

	@BeforeClass
	public static void setUpOnce() {
		context = SciJavaGateway.get().getGlobalContext();
	}

	@Before
	public void setUp() {
		context.inject(this);
	}

	@Test
	public void testModuleExecution() throws Exception {
		assertNotNull(m_commandService);

		NodeModule commandModule = m_nodeModuleService.createNodeModule(new CommandInfo(MyCommand.class), null, m_spec,
				NodeLogger.getLogger(NodeModuleTest.class));
		assertNotNull(commandModule);

		// assertFalse("Command was cancelled: " +
		// commandModule.getCancelReason(), commandModule.isCanceled());

		ArrayList<DataCell[]> cells = new ArrayList<>();
		CellOutput cellOutput = new CellOutput() {

			ArrayList<DataCell[]> m_cells = cells;

			@Override
			public void push(DataCell[] cells) throws InterruptedException {
				assertNotNull(cells);
				assertEquals(cells.length, 7);

				assertTrue("Boolean output was not extracted correctly!", ((BooleanCell) cells[0]).getBooleanValue());
				assertEquals("Byte output was not extracted correctly!", 42, ((IntCell) cells[1]).getIntValue());
				assertEquals("Short output was not extracted correctly!", 420, ((IntCell) cells[2]).getIntValue());
				assertEquals("Integer output was not extracted correctly!", 42000, ((IntCell) cells[3]).getIntValue());
				assertEquals("Long output was not extracted correctly!", 4200000, ((LongCell) cells[4]).getLongValue());
				assertEquals("String output was not extracted correctly!", "KNIME",
						((StringCell) cells[5]).getStringValue());
				assertEquals("Character output was not extracted correctly!", " ",
						((StringCell) cells[6]).getStringValue());

				m_cells.add(cells);
			}
		};
		commandModule.run(m_testRow, cellOutput, null);

		assertFalse("No cells were pushed", cells.isEmpty());
	}

	/**
	 * Test command which verifies that inputs have been filled by the
	 * preprocessor and feeds them directly back in the outputs which can then
	 * be collected into a data row.
	 *
	 * @author Jonathan Hale
	 */
	public static class MyCommand implements Command {

		@Parameter(type = ItemIO.INPUT)
		Boolean b;
		@Parameter(type = ItemIO.INPUT)
		Byte by;
		@Parameter(type = ItemIO.INPUT)
		Short s;
		@Parameter(type = ItemIO.INPUT)
		Integer i;
		@Parameter(type = ItemIO.INPUT)
		Long l;
		@Parameter(type = ItemIO.INPUT)
		String str;
		@Parameter(type = ItemIO.INPUT)
		Character c;

		@Parameter(type = ItemIO.OUTPUT)
		Boolean ob;
		@Parameter(type = ItemIO.OUTPUT)
		Byte oby;
		@Parameter(type = ItemIO.OUTPUT)
		Short os;
		@Parameter(type = ItemIO.OUTPUT)
		Integer oi;
		@Parameter(type = ItemIO.OUTPUT)
		Long ol;
		@Parameter(type = ItemIO.OUTPUT)
		String ostr;
		@Parameter(type = ItemIO.OUTPUT)
		Character oc;

		@Override
		public void run() {
			assertTrue("Boolean input was not filled correctly!", b);
			assertEquals("Byte input was not filled correctly!", new Byte((byte) 42), by);
			assertEquals("Short input was not filled correctly!", new Short((short) 420), s);
			assertEquals("Integer input was not filled correctly!", new Integer(42000), i);
			assertEquals("Long input was not filled correctly!", new Long(4200000), l);
			assertEquals("String input was not filled correctly!", "KNIME", str);
			assertEquals("Character input was not filled correctly!", new Character(' '), c);

			ob = b;
			oby = by;
			os = s;
			oi = i;
			ol = l;
			ostr = str;
			oc = c;
		}

	}
}
