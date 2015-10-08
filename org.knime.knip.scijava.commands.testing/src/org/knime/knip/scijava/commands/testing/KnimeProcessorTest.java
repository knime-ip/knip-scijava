package org.knime.knip.scijava.commands.testing;

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
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.container.DataContainer;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.LongCell;
import org.knime.core.data.def.StringCell;
import org.knime.knip.scijava.commands.DefaultKnimePostprocessor;
import org.knime.knip.scijava.commands.KnimeInputDataTableService;
import org.knime.knip.scijava.commands.KnimeOutputDataTableService;
import org.knime.knip.scijava.commands.adapter.InputAdapterService;
import org.knime.knip.scijava.commands.adapter.OutputAdapterService;
import org.knime.knip.scijava.commands.mapping.ColumnInputMappingKnimePreprocessor;
import org.knime.knip.scijava.commands.mapping.ColumnModuleItemMappingService;
import org.knime.knip.scijava.core.ResourceAwareClassLoader;
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
	KnimeInputDataTableService m_inputTableService;
	@Parameter
	KnimeOutputDataTableService m_outputTableService;
	@Parameter
	ColumnModuleItemMappingService m_cimService;

	protected static List<Class<? extends Service>> requiredServices = Arrays.<Class<? extends Service>> asList(
			KnimeInputDataTableService.class, KnimeOutputDataTableService.class, CommandService.class,
			ColumnModuleItemMappingService.class, InputAdapterService.class, OutputAdapterService.class);

	private static final DataTableSpec m_spec;
	private static final DataContainer m_container;

	static {
		// Create the test table
		m_spec = new DataTableSpec("TestTable", new String[] { "b", "by", "s", "i", "l", "str", "c" },
				new DataType[] { BooleanCell.TYPE, IntCell.TYPE, IntCell.TYPE, IntCell.TYPE, LongCell.TYPE,
						StringCell.TYPE, StringCell.TYPE });
		m_container = new DataContainer(m_spec);
		m_container.addRowToTable(
				new DefaultRow(new RowKey("TestRow001"), BooleanCell.TRUE, new IntCell(42), new IntCell(420),
						new IntCell(42000), new LongCell(4200000), new StringCell("KNIME"), new StringCell(" ")));

		m_container.close();
	}

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

		assertNotNull(m_inputTableService);
		assertNotNull(m_outputTableService);
		assertNotNull(m_commandService);
		m_inputTableService.setInputDataTable(m_container.getTable());
		m_outputTableService.setOutputContainer(new DataContainer(m_spec));
		m_inputTableService.next();

		Future<CommandModule> command = m_commandService.run(MyCommand.class, true);
		assertNotNull(command);
		CommandModule commandModule = command.get();
		assertNotNull(commandModule);
		assertFalse("Command was cancelled: " + commandModule.getCancelReason(), commandModule.isCanceled());

		m_outputTableService.appendRow();
		m_outputTableService.getDataContainer().close();
		DataTable table = m_outputTableService.getDataContainer().getTable();

		DataRow row = table.iterator().next();
		assertNotNull(row);

		assertTrue("Boolean output was not extracted correctly!", ((BooleanCell) row.getCell(0)).getBooleanValue());
		assertEquals("Byte output was not extracted correctly!", 42, ((IntCell) row.getCell(1)).getIntValue());
		assertEquals("Short output was not extracted correctly!", 420, ((IntCell) row.getCell(2)).getIntValue());
		assertEquals("Integer output was not extracted correctly!", 42000, ((IntCell) row.getCell(3)).getIntValue());
		assertEquals("Long output was not extracted correctly!", 4200000, ((LongCell) row.getCell(4)).getLongValue());
		assertEquals("String output was not extracted correctly!", "KNIME",
				((StringCell) row.getCell(5)).getStringValue());
		assertEquals("Character output was not extracted correctly!", " ",
				((StringCell) row.getCell(6)).getStringValue());
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
