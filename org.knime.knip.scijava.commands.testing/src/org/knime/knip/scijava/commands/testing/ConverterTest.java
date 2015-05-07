package org.knime.knip.scijava.commands.testing;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.knime.core.data.DataCell;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.LongCell;
import org.knime.core.data.def.StringCell;
import org.knime.knip.scijava.commands.adapter.OutputAdapter;
import org.knime.knip.scijava.commands.adapter.OutputAdapterPlugin;
import org.knime.knip.scijava.commands.adapter.basic.BooleanOutputAdapter;
import org.knime.knip.scijava.commands.adapter.basic.ByteOutputAdapter;
import org.knime.knip.scijava.commands.adapter.basic.CharOutputAdapter;
import org.knime.knip.scijava.commands.adapter.basic.IntOutputAdapter;
import org.knime.knip.scijava.commands.adapter.basic.LongOutputAdapter;
import org.knime.knip.scijava.commands.adapter.basic.ShortOutputAdapter;
import org.knime.knip.scijava.commands.adapter.basic.StringOutputAdapter;
import org.knime.knip.scijava.core.ResourceAwareClassLoader;
import org.scijava.Context;
import org.scijava.convert.ConvertService;
import org.scijava.convert.Converter;
import org.scijava.convert.DefaultConvertService;
import org.scijava.plugin.DefaultPluginFinder;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.PluginIndex;
import org.scijava.service.Service;

/**
 * Test cases for testing if {@link DefaultConvertService} detects the
 * {@link OutputAdapter}s as {@link Converter}s correctly
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
public class ConverterTest {

	@Parameter
	ConvertService convertService;

	private static Context context;

	protected static List<Class<? extends Service>> requiredServices = Arrays
			.<Class<? extends Service>> asList(DefaultConvertService.class);

	@BeforeClass
	public static void setUpOnce() {
		ResourceAwareClassLoader cl = new ResourceAwareClassLoader(
				ConverterTest.class.getClassLoader(), ConverterTest.class);

		context = new Context(requiredServices, new PluginIndex(
				new DefaultPluginFinder(cl)));
	}

	@AfterClass
	public static void tearDown() {
		context.dispose();
	}

	@Before
	public void setUp() {
		context.inject(this);
	}

	public <T> void testOutputAdapterConversion(T o,
			Class<? extends OutputAdapterPlugin<?, ?>> oa,
			Class<? extends DataCell> cellClass) {
		assertNotNull("Plugin " + oa.getClass().getName()
				+ " could not be found",
				convertService.getInstance(BooleanOutputAdapter.class));

		DataCell c = convertService.convert(o, cellClass);

		assertNotNull("Conversion from " + o.getClass().getName() + " failed",
				c);
	}

	@Test
	public void testBooleanOutputAdapter() {
		testOutputAdapterConversion(new Boolean(true),
				BooleanOutputAdapter.class, BooleanCell.class);
	}

	@Test
	public void testIntOutputAdapter() {
		testOutputAdapterConversion(new Integer(42), IntOutputAdapter.class,
				IntCell.class);
	}

	@Test
	public void testLongOutputAdapter() {
		testOutputAdapterConversion(new Long(42), LongOutputAdapter.class,
				LongCell.class);
	}

	@Test
	public void testStringOutputAdapter() {
		testOutputAdapterConversion(
				new String("This is not a String. Really."),
				StringOutputAdapter.class, StringCell.class);
	}

	@Test
	public void testShortOutputAdapter() {
		testOutputAdapterConversion(new Short((short) 42),
				ShortOutputAdapter.class, IntCell.class);
	}

	@Test
	public void testByteOutputAdapter() {
		testOutputAdapterConversion(new Byte((byte) 42),
				ByteOutputAdapter.class, IntCell.class);
	}

	@Test
	public void testCharOutputAdapter() {
		testOutputAdapterConversion(new Character(' '),
				CharOutputAdapter.class, StringCell.class);
	}

}
