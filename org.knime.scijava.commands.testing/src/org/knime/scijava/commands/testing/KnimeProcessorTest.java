package org.knime.scijava.commands.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.AfterClass;
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
import org.knime.scijava.commands.adapter.InputAdapterService;
import org.knime.scijava.commands.adapter.OutputAdapterService;
import org.knime.scijava.commands.io.DefaultInputDataRowService;
import org.knime.scijava.commands.io.DefaultOutputDataRowService;
import org.knime.scijava.commands.io.InputDataRowService;
import org.knime.scijava.commands.io.OutputDataRowService;
import org.knime.scijava.commands.mapping.ColumnModuleItemMappingService;
import org.knime.scijava.commands.mapping.process.ColumnInputMappingKnimePreprocessor;
import org.knime.scijava.commands.mapping.process.DefaultKnimePostprocessor;
import org.knime.scijava.core.ResourceAwareClassLoader;
import org.scijava.Context;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.command.CommandModule;
import org.scijava.command.CommandService;
import org.scijava.plugin.DefaultPluginFinder;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.PluginIndex;
import org.scijava.plugin.PluginService;
import org.scijava.service.Service;

/**
 * Test for {@link ColumnInputMappingKnimePreprocessor} and
 * {@link DefaultKnimePostprocessor}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
public class KnimeProcessorTest {

	private static Context context;

	@Parameter
	CommandService m_commandService;
	@Parameter
	InputDataRowService m_inputRowService;
	@Parameter
	OutputDataRowService m_outputCellsService;
	@Parameter
	ColumnModuleItemMappingService m_cimService;

	protected static List<Class<? extends Service>> requiredServices = Arrays.<Class<? extends Service>> asList(
			DefaultInputDataRowService.class, DefaultOutputDataRowService.class, CommandService.class,
			ColumnModuleItemMappingService.class, InputAdapterService.class, OutputAdapterService.class);

	// Create the test table
	private static final DataRow m_testRow = new DefaultRow(new RowKey("TestRow001"), BooleanCell.TRUE, new IntCell(42),
			new IntCell(420), new IntCell(42000), new LongCell(4200000), new StringCell("KNIME"), new StringCell(" "));

	private static final DataTableSpec m_spec = new DataTableSpec(new String[] { "b", "by", "s", "i", "l", "str", "c" },
			new DataType[] { BooleanCell.TYPE, IntCell.TYPE, IntCell.TYPE, IntCell.TYPE, LongCell.TYPE, StringCell.TYPE,
					StringCell.TYPE });

	@BeforeClass
	public static void setUpOnce() {
		ResourceAwareClassLoader cl = new ResourceAwareClassLoader(InputAdapterConverterTest.class.getClassLoader(),
				InputAdapterConverterTest.class);

		context = new Context(requiredServices, new PluginIndex(new DefaultPluginFinder(cl)));
	}

	@AfterClass
	public static void tearDown() {
		context.dispose();
	}

	@Before
	public void setUp() {
		context.inject(this);

		// add test mapping
		m_cimService.addMapping("b", "b");
		m_cimService.addMapping("by", "by");
		m_cimService.addMapping("s", "s");
		m_cimService.addMapping("i", "i");
		m_cimService.addMapping("l", "l");
		m_cimService.addMapping("str", "str");
		m_cimService.addMapping("c", "c");
	}

	@Test
	public void testModuleProcessing() throws InterruptedException, ExecutionException {
		// check if the ColumnInputMappingPreprocessor plugin was found:
		assertNotNull("ColumnInputMappingPreprocessor was not found.",
				context.getService(PluginService.class).getPlugin(ColumnInputMappingKnimePreprocessor.class));
		assertNotNull("DefaultKnimePostprocessor was not found.",
				context.getService(PluginService.class).getPlugin(DefaultKnimePostprocessor.class));

		assertNotNull(m_inputRowService);
		assertNotNull(m_outputCellsService);
		assertNotNull(m_commandService);
		m_inputRowService.setInputDataRow(m_testRow);
		m_inputRowService.setDataTableSpec(m_spec);

		Future<CommandModule> command = m_commandService.run(MyCommand.class, true);
		assertNotNull(command);
		CommandModule commandModule = command.get();
		assertNotNull(commandModule);
		assertFalse("Command was cancelled: " + commandModule.getCancelReason(), commandModule.isCanceled());

		DataCell[] cells = m_outputCellsService.getOutputDataCells();
		assertNotNull(cells);
		assertEquals(7, cells.length);

		assertTrue("Boolean output was not extracted correctly!", ((BooleanCell) cells[0]).getBooleanValue());
		assertEquals("Byte output was not extracted correctly!", 42, ((IntCell) cells[1]).getIntValue());
		assertEquals("Short output was not extracted correctly!", 420, ((IntCell) cells[2]).getIntValue());
		assertEquals("Integer output was not extracted correctly!", 42000, ((IntCell) cells[3]).getIntValue());
		assertEquals("Long output was not extracted correctly!", 4200000, ((LongCell) cells[4]).getLongValue());
		assertEquals("String output was not extracted correctly!", "KNIME", ((StringCell) cells[5]).getStringValue());
		assertEquals("Character output was not extracted correctly!", " ", ((StringCell) cells[6]).getStringValue());
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
