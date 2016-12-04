package org.knime.scijava.commands.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.MissingCell;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.LongCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.scijava.commands.CellOutput;
import org.knime.scijava.commands.SciJavaGateway;
import org.knime.scijava.commands.module.NodeModule;
import org.knime.scijava.commands.module.NodeModuleService;
import org.knime.scijava.commands.settings.models.SettingsModelColumnSelection;
import org.scijava.Context;
import org.scijava.command.CommandInfo;
import org.scijava.plugin.Parameter;

/**
 * Test for {@link ColumnInputMappingKnimePreprocessor} and
 * {@link DefaultKnimePostprocessor}.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
public class NodeModuleTest {

	static Context context;

	@Parameter
	NodeModuleService m_nodeModuleService;

	// Create the test table
	final String[] inputNames = new String[] { "b", "i", "l", "str" };
	final String[] outputNames = new String[] { "ob", "oi", "ol", "ostr" };

	final DataType[] inputDataTypes = new DataType[] { BooleanCell.TYPE, IntCell.TYPE, LongCell.TYPE, StringCell.TYPE };
	final DataTableSpec m_spec = new DataTableSpec(new String[] { "b", "i", "l", "str" }, inputDataTypes);
	final DataType[] expectedOutputDataTypes = new DataType[] { BooleanCell.TYPE, IntCell.TYPE, LongCell.TYPE,
			StringCell.TYPE };

	@BeforeClass
	public static void setUpOnce() {
		context = SciJavaGateway.get().getGlobalContext();
	}

	@Before
	public void setUp() {
		context.inject(this);
	}

	/**
	 * Create a NodeModule
	 * @param emptyTest Whether to have the module check for correct input values
	 * @return the created Module
	 */
	private NodeModule createNodeModule(boolean emptyTest) {
		final HashMap<String, SettingsModel> settings = new HashMap<String, SettingsModel>();
		for (final String name : inputNames) {
			settings.put(name, new SettingsModelColumnSelection(name, name));
		}
		
		settings.put("emptyTest", new SettingsModelBoolean("emptyTest", emptyTest));

		for (int i = 0; i < outputNames.length; ++i) {
			settings.put(outputNames[i], new SettingsModelString(outputNames[i], expectedOutputDataTypes[i].getName()));
		}

		return m_nodeModuleService.createNodeModule(new CommandInfo(ScijavaTestCommand.class), settings, m_spec,
				NodeLogger.getLogger(NodeModuleTest.class));
	}

	@Test
	public void testExecution() throws Exception {
		final NodeModule nodeModule = createNodeModule(false);
		
		final DataRow testRow = new DefaultRow( //
				new RowKey("TestRow001"), //
				BooleanCell.TRUE, //
				new IntCell(42000), //
				new LongCell(4200000), //
				new StringCell("KNIME"));

		final ArrayList<DataCell[]> cells = new ArrayList<>();
		final CellOutput cellOutput = new CellOutput() {

			final ArrayList<DataCell[]> m_cells = cells;

			@Override
			public void push(DataCell[] cells) throws InterruptedException {
				assertNotNull(cells);
				assertEquals("Unexpected amout of cells pushed.", 4, cells.length);

				assertEquals("Unexpected type for pushed cell.", BooleanCell.class, cells[0].getClass());
				assertEquals("Unexpected type for pushed cell.", IntCell.class, cells[1].getClass());
				assertEquals("Unexpected type for pushed cell.", LongCell.class, cells[2].getClass());
				assertEquals("Unexpected type for pushed cell.", StringCell.class, cells[3].getClass());

				assertTrue("Boolean output was not extracted correctly!", ((BooleanCell) cells[0]).getBooleanValue());
				assertEquals("Integer output was not extracted correctly!", 42000, ((IntCell) cells[1]).getIntValue());
				assertEquals("Long output was not extracted correctly!", 4200000, ((LongCell) cells[2]).getLongValue());
				assertEquals("String output was not extracted correctly!", "KNIME",
						((StringCell) cells[3]).getStringValue());

				m_cells.add(cells);
			}
		};

		nodeModule.run(testRow, cellOutput, null);

		assertFalse("No cells were pushed", cells.isEmpty());
	}

	@Test
	public void testMissings() throws Exception {
		final NodeModule nodeModule = createNodeModule(true);

		final DataRow emptyRow = new DefaultRow( //
				new RowKey("TestRow001"), //
				new MissingCell("Nothing here."), //
				new MissingCell("Nothing here either."), //
				new MissingCell("Full of nihilism."), //
				new MissingCell("Void."));

		final ArrayList<DataCell[]> cells = new ArrayList<>();
		final CellOutput cellOutput = new CellOutput() {

			final ArrayList<DataCell[]> m_cells = cells;

			@Override
			public void push(DataCell[] cells) throws InterruptedException {
				assertNotNull(cells);
				assertEquals("Unexpected amout of cells pushed.", 4, cells.length);

				for (DataCell cell : cells) {
					assertEquals("Unexpected type for pushed cell.", MissingCell.class, cell.getClass());
				}

				m_cells.add(cells);
			}
		};

		nodeModule.run(emptyRow, cellOutput, null);

		assertFalse("No cells were pushed", cells.isEmpty());
	}
}
