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
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.container.DataContainer;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.LongCell;
import org.knime.core.data.def.StringCell;
import org.knime.knip.scijava.commands.KnimeInputDataTableService;
import org.knime.knip.scijava.commands.adapter.InputAdapterService;
import org.knime.knip.scijava.commands.mapping.ColumnInputMappingKnimePreprocessor;
import org.knime.knip.scijava.commands.mapping.ColumnToModuleItemMappingService;
import org.knime.knip.scijava.core.ResourceAwareClassLoader;
import org.scijava.Context;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.command.CommandModule;
import org.scijava.command.CommandService;
import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.DefaultPluginFinder;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.PluginIndex;
import org.scijava.plugin.PluginInfo;
import org.scijava.plugin.PluginService;
import org.scijava.service.Service;

/**
 * Test for {@link ColumnInputMappingKnimePreprocessor}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
public class ColumnInputMappingPreprocessorTest {

	private static Context context;

	@Parameter
	CommandService m_commandService;
	@Parameter
	KnimeInputDataTableService m_dataTableService;
	@Parameter
	ColumnToModuleItemMappingService m_cimService;

	protected static List<Class<? extends Service>> requiredServices = Arrays.<Class<? extends Service>> asList(
			KnimeInputDataTableService.class, CommandService.class, ColumnToModuleItemMappingService.class,
			InputAdapterService.class);


	private static final DataContainer m_container;

	static {
		// Create the test table
		DataTableSpec spec = new DataTableSpec("TestTable", new String[] { "b", "by", "s", "i", "l", "str", "c" },
				new DataType[] { BooleanCell.TYPE, IntCell.TYPE, IntCell.TYPE, IntCell.TYPE, LongCell.TYPE,
						StringCell.TYPE, StringCell.TYPE });
		m_container = new DataContainer(spec);
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

		m_cimService.addMapping("b", "b");
		m_cimService.addMapping("by", "by");
		m_cimService.addMapping("s", "s");
		m_cimService.addMapping("i", "i");
		m_cimService.addMapping("l", "l");
		m_cimService.addMapping("str", "str");
		m_cimService.addMapping("c", "c");
	}

	@Test
	public void testModulePreprocessing() throws InterruptedException, ExecutionException {
		// check if the ColumnInputMappingPreprocessor plugin was found:
		assertNotNull("ColumnInputMappingPreprocessor was not found.", context.getService(PluginService.class).getPlugin(ColumnInputMappingKnimePreprocessor.class));
		
		assertNotNull(m_dataTableService);
		m_dataTableService.setInputDataTable(m_container.getTable());
		assertNotNull(m_commandService);
		
		m_dataTableService.next();
		
		Future<CommandModule> command = m_commandService.run(MyCommand.class, true);
		assertNotNull(command);
		CommandModule commandModule = command.get();
		assertNotNull(commandModule);
		assertFalse("Command was cancelled: " + commandModule.getCancelReason(), commandModule.isCanceled());
	}

	/**
	 * Test command which verifies that inputs have been filled by the
	 * preprocessor.
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

		@Override
		public void run() {
			assertTrue("Boolean input was not filled correctly!", b);
			assertEquals("Byte input was not filled correctly!", new Byte((byte) 42), by);
			assertEquals("Short input was not filled correctly!", new Short((short) 420), s);
			assertEquals("Integer input was not filled correctly!", new Integer(42000), i);
			assertEquals("Long input was not filled correctly!", new Long(4200000), l);
			assertEquals("String input was not filled correctly!", "KNIME", str);
			assertEquals("Character input was not filled correctly!", new Character(' '), c);
		}

	}
}
