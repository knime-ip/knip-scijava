package org.knime.scijava.commands.testing.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.LongCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.scijava.commands.SciJavaGateway;
import org.knime.scijava.commands.module.NodeModuleService;
import org.knime.scijava.commands.settings.models.SettingsModelColumnSelection;
import org.knime.scijava.commands.testing.ScijavaTestCommand;
import org.scijava.Context;
import org.scijava.command.CommandInfo;
import org.scijava.plugin.Parameter;

/**
 * Test for {@link NodeModuleService}.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
public class DefaultNodeModuleServiceTest {

	private static Context context;

	@Parameter
	NodeModuleService m_nodeModuleService;

	private final DataTableSpec inSpec = new DataTableSpec(new String[] { "b", "i", "l", "str" },
			new DataType[] { BooleanCell.TYPE, IntCell.TYPE, LongCell.TYPE, StringCell.TYPE });

	@BeforeClass
	public static void setUpOnce() {
		context = SciJavaGateway.get().getGlobalContext();
	}

	@Before
	public void setUp() {
		context.inject(this);
		assertNotNull(m_nodeModuleService);
	}

	@Test
	public void testCreateOutSpec() throws Exception {
		/*
		 * Create settings which just map input columns to equally named module
		 * items.
		 */
		final HashMap<String, SettingsModel> settings = new HashMap<String, SettingsModel>();
		for (final String name : new String[] { "b", "i", "l", "str" }) {
			settings.put(name, new SettingsModelColumnSelection(name, name));
		}

		String[] outNames = new String[] { "ob", "oi", "ol", "ostr" };
		DataType[] outTypes = new DataType[] { BooleanCell.TYPE, IntCell.TYPE, LongCell.TYPE, StringCell.TYPE };
		for (int i = 0; i < outNames.length; ++i) {
			settings.put(outNames[i], new SettingsModelString(outNames[i], outTypes[i].getName()));
		}

		// Produce output column spec
		DataTableSpec outSpec = m_nodeModuleService.createOutSpec(new CommandInfo(ScijavaTestCommand.class), settings,
				inSpec);

		assertArrayEquals("Names for output columns are incorrect.", outNames, outSpec.getColumnNames());
		for (int i = 0; i < outNames.length; ++i) {
			assertEquals("Type for column " + i + " is incorrect.", outTypes[i], outSpec.getColumnSpec(i).getType());
		}
	}
}
