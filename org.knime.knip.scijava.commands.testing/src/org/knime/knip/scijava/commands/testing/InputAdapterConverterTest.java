package org.knime.knip.scijava.commands.testing;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.knime.core.data.DataValue;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.knip.scijava.commands.adapter.InputAdapter;
import org.knime.knip.scijava.commands.adapter.basic.BooleanInputAdapter;
import org.knime.knip.scijava.commands.adapter.basic.ByteInputAdapter;
import org.knime.knip.scijava.commands.adapter.basic.CharInputAdapter;
import org.knime.knip.scijava.commands.adapter.basic.IntInputAdapter;
import org.knime.knip.scijava.commands.adapter.basic.LongInputAdapter;
import org.knime.knip.scijava.commands.adapter.basic.ShortInputAdapter;
import org.knime.knip.scijava.commands.adapter.basic.StringInputAdapter;
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
 * {@link InputAdapter}s as {@link Converter}s correctly
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
public class InputAdapterConverterTest {

	@Parameter
	ConvertService convertService;

	private static Context context;

	protected static List<Class<? extends Service>> requiredServices = Arrays
			.<Class<? extends Service>> asList(DefaultConvertService.class);

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
	}

	public <T extends DataValue> void testInputAdapterConversion(T dataValue, Class<? extends InputAdapter<?, ?>> ia,
			Class<?> valueClass) {
		assertNotNull("Plugin " + ia.getClass().getName() + " could not be found",
				convertService.getInstance(ia));

		Object o = convertService.convert(dataValue, valueClass);

		assertNotNull("Conversion from " + dataValue.getClass().getName() + " failed", o);
	}

	@Test
	public void testBooleanInputAdapter() {
		testInputAdapterConversion(BooleanCell.TRUE, BooleanInputAdapter.class, Boolean.class);
	}

	@Test
	public void testIntInputAdapter() {
		testInputAdapterConversion(new IntCell(42), IntInputAdapter.class, Integer.class);
	}

	@Test
	public void testLongInputAdapter() {
		testInputAdapterConversion(new IntCell(42), LongInputAdapter.class, Long.class);
	}

	@Test
	public void testStringInputAdapter() {
		testInputAdapterConversion(new StringCell("This is not a String. Really."), StringInputAdapter.class,
				String.class);
	}

	@Test
	public void testShortInputAdapter() {
		testInputAdapterConversion(new IntCell((short) 42), ShortInputAdapter.class, Short.class);
	}

	@Test
	public void testByteInputAdapter() {
		testInputAdapterConversion(new IntCell((byte) 42), ByteInputAdapter.class, Byte.class);
	}

	@Test
	public void testCharInputAdapter() {
		testInputAdapterConversion(new StringCell(" "), CharInputAdapter.class, Character.class);
	}

}
