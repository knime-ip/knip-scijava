package org.knime.scijava.base.node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.apache.commons.lang3.ClassUtils;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.convert.ConversionKey;
import org.knime.core.data.convert.datacell.JavaToDataCellConverter;
import org.knime.core.data.convert.datacell.JavaToDataCellConverterFactory;
import org.knime.core.data.convert.datacell.JavaToDataCellConverterRegistry;
import org.knime.core.data.convert.java.DataCellToJavaConverter;
import org.knime.core.data.convert.java.DataCellToJavaConverterRegistry;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.streamable.BufferedDataTableRowOutput;
import org.knime.core.node.streamable.DataTableRowInput;
import org.knime.core.node.streamable.InputPortRole;
import org.knime.core.node.streamable.OutputPortRole;
import org.knime.core.node.streamable.PartitionInfo;
import org.knime.core.node.streamable.PortInput;
import org.knime.core.node.streamable.PortOutput;
import org.knime.core.node.streamable.RowInput;
import org.knime.core.node.streamable.RowOutput;
import org.knime.core.node.streamable.StreamableOperator;
import org.knime.core.util.UniqueNameGenerator;
import org.scijava.param2.ParameterStructs;
import org.scijava.param2.ValidityException;
import org.scijava.struct2.Member;
import org.scijava.struct2.Struct;

public class RowToRowFunctionNodeModel<I, O> extends NodeModel {

	private final NodeStructInstance<Function<I, O>> m_funcInstance;

	private final Struct m_inStruct;

	private final Struct m_outStruct;

	private DataRowToObject<I> m_inputConversion;

	private List<MemberToDataCellConversionInfoHelper<?>> m_outputConversionInfoHelpers;

	protected RowToRowFunctionNodeModel(final Function<I, O> func) throws ValidityException {
		super(1, 1);
		m_funcInstance = new NodeDialogStructInstance<>(ParameterStructs.structOf(func.getClass()), func);
		// TODO: handle 'null' raw types
		m_inStruct = ParameterStructs.structOf(m_funcInstance.member("input").member().getRawType());
		m_outStruct = ParameterStructs.structOf(m_funcInstance.member("output").member().getRawType());
	}

	@Override
	public InputPortRole[] getInputPortRoles() {
		return new InputPortRole[] { InputPortRole.DISTRIBUTED_STREAMABLE };
	}

	@Override
	public OutputPortRole[] getOutputPortRoles() {
		return new OutputPortRole[] { OutputPortRole.DISTRIBUTED };
	}

	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		try {
			configureInputConverters(inSpecs[0]);
			configureOutputConverters();
			return new DataTableSpec[] { createOutputSpec(inSpecs[0]) };
		} catch (final Exception e) {
			throw new InvalidSettingsException(e);
		}
	}

	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		final DataTableRowInput input = new DataTableRowInput(inData[0]);
		final BufferedDataTableRowOutput output = new BufferedDataTableRowOutput(
				exec.createDataContainer(createOutputSpec(inData[0].getSpec())));
		executeInternal(input, output, exec);
		return new BufferedDataTable[] { output.getDataTable() };
	}

	@Override
	public StreamableOperator createStreamableOperator(final PartitionInfo partitionInfo,
			final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new StreamableOperator() {

			@Override
			public void runFinal(final PortInput[] inputs, final PortOutput[] outputs, final ExecutionContext exec)
					throws Exception {
				final RowInput input = (RowInput) inputs[0];
				final RowOutput output = (RowOutput) outputs[0];
				executeInternal(input, output, exec);
			}
		};
	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		try {
			m_funcInstance.saveSettingsTo(settings);
		} catch (final InvalidSettingsException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		// TODO
	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_funcInstance.loadSettingsFrom(settings);

		// load settings into func (w/o special params)
		// load mapping of special params

		// Read mappings
		// ConfigBase conversionInfos =
		// settings.getConfigBase("input-conversion-infos");
		// int numMappings = conversionInfos.getInt("number-of-infos");
		// m_valueToStruct = new ArrayList<>();
		// for (int i = 0; i < numMappings; i++) {
		// ConfigBase converterEntry = conversionInfos.getConfigBase("info-" +
		// i);
		// int index = converterEntry.getInt("column-index");
		// String memberName = converterEntry.getString("member-name");
		// try {
		// @SuppressWarnings("unchecked")
		// Function<DataValue, ?> conv = ((Function<DataValue, ?>) Class
		// .forName(converterEntry.getString("converter")).newInstance());
		// m_valueToStruct.add(new
		// DefaultDataValueToStructConversionInfo<>(index, memberName, conv));
		// } catch (Exception e) {
		// // FIXME
		// e.printStackTrace();
		// }
		// }

		// same for output infos
		// try {
		// m_rowToObject = new DataRowToObject(m_func.members(), null);
		// m_objectToCells = new ObjectToDataCells<O>(m_outType, null);
		// } catch (ValidityException | InstantiationException |
		// IllegalAccessException e) {
		// throw new InvalidSettingsException("Problem parsing struct.", e);
		// }
	}

	@Override
	protected void loadInternals(final File nodeInternDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// no op
	}

	@Override
	protected void saveInternals(final File nodeInternDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// no op
	}

	@Override
	protected void reset() {
		// no op
	}

	private void configureInputConverters(final DataTableSpec inSpec) throws InvalidSettingsException {
		final List<Member<?>> members = m_inStruct.members();
		final ArrayList<DataValueToMemberConversionInfo<?, ?>> conversionInfos = new ArrayList<>(members.size());
		final DataCellToJavaConverterRegistry converters = DataCellToJavaConverterRegistry.getInstance();
		// maintain a local cache to speed up converter lookup
		final HashMap<ConversionKey, DataCellToJavaConverter<?, ?>> converterCache = new HashMap<>(members.size());
		for (int i = 0; i < members.size(); i++) {
			final Member<?> member = members.get(i);
			// TODO: hard-coded for now, we'll get the source type and column
			// index via the user-selected column mapping
			final DataType sourceType = DoubleCell.TYPE;
			final int sourceColIndex = 0;
			final Class<?> destinationType = ClassUtils.primitiveToWrapper(member.getRawType());
			// TODO: handle 'null' cell classes, also see documentation of
			// getCellClass for type-cast stuff we need to
			// consider
			final ConversionKey conversionKey = new ConversionKey(sourceType.getCellClass(), destinationType);
			DataCellToJavaConverter<?, ?> converter = converterCache.get(conversionKey);
			if (converter == null) {
				converter = converters.getPreferredConverterFactory(sourceType, destinationType)
						.orElseThrow(() -> new InvalidSettingsException("No converter found to convert from '"
								+ sourceType.getName() + "' to '" + destinationType.getSimpleName() + "'."))
						.create();
				converterCache.put(conversionKey, converter);
			}
			final DataCellToJavaConverter<?, ?> finalConverter = converter;
			final DataValueToMemberConversionInfo<?, ?> conversionInfo = new DefaultDataValueToMemberConversionInfo<>(
					sourceColIndex, member.getKey(), t -> {
						try {
							return finalConverter.convertUnsafe((DataCell) t);
						} catch (final Exception e) {
							throw new ConversionException();
						}
					});
			conversionInfos.add(conversionInfo);
		}
		// TODO: handle 'null' raw types
		// TODO: type-safety
		final Class<I> inputType = (Class<I>) m_funcInstance.member("input").member().getRawType();
		try {
			m_inputConversion = new DataRowToObject<>(inputType, conversionInfos);
		} catch (final ValidityException | InstantiationException | IllegalAccessException e) {
			throw new InvalidSettingsException(e);
		}
	}

	private void configureOutputConverters() {
		final List<Member<?>> members = m_outStruct.members();
		m_outputConversionInfoHelpers = new ArrayList<>(members.size());
		final JavaToDataCellConverterRegistry converters = JavaToDataCellConverterRegistry.getInstance();
		// maintain a local cache to speed up converter lookup
		final HashMap<Class<?>, JavaToDataCellConverterFactory<?>> converterCache = new HashMap<>(members.size());
		for (int i = 0; i < members.size(); i++) {
			final Member<?> member = members.get(i);
			// TODO: handle 'null' raw types
			final Class<?> sourceType = ClassUtils.primitiveToWrapper(member.getRawType());
			JavaToDataCellConverterFactory<?> factory = converterCache.get(sourceType);
			if (factory == null) {
				factory = converters.getFactoriesForSourceType(sourceType).iterator().next();
				converterCache.put(sourceType, factory);
			}
			final MemberToDataCellConversionInfoHelper<?> conversionInfo = new MemberToDataCellConversionInfoHelper<>(
					member.getKey(), factory);
			m_outputConversionInfoHelpers.add(conversionInfo);
		}
	}

	private DataTableSpec createOutputSpec(final DataTableSpec inSpec) throws Exception {
		// TODO: array size is only correct if every member of the output struct
		// represents a column
		final List<Member<?>> members = m_outStruct.members();
		final UniqueNameGenerator gen = new UniqueNameGenerator(inSpec);
		final DataColumnSpec[] outputColumnSpecs = new DataColumnSpec[m_outStruct.members().size()];
		for (int i = 0; i < members.size(); i++) {
			final MemberToDataCellConversionInfoHelper<?> helper = m_outputConversionInfoHelpers.get(i);
			assert Objects.equals(helper.m_memberName, members.get(i).getKey());
			outputColumnSpecs[i] = gen.newColumn(helper.m_memberName, helper.m_factory.getDestinationType());
		}
		return new DataTableSpec(outputColumnSpecs);
	}

	private void executeInternal(final RowInput input, final RowOutput output, final ExecutionContext exec)
			throws InvalidSettingsException, InterruptedException {
		try {
			final ArrayList<MemberToDataCellConversionInfo<?>> outputConversionInfos = new ArrayList<>(
					m_outputConversionInfoHelpers.size());
			for (int i = 0; i < m_outputConversionInfoHelpers.size(); i++) {
				final MemberToDataCellConversionInfoHelper<?> helper = m_outputConversionInfoHelpers.get(i);
				final JavaToDataCellConverterFactory<?> factory = helper.m_factory;
				// TODO: don't create a dedicated converter for each column,
				// they can be shared type-safety
				final JavaToDataCellConverter converter = factory.create(exec);
				final MemberToDataCellConversionInfo<?> outputConversionInfo = new DefaultMemberToDataCellConversionInfo<>(
						helper.m_memberName, factory.getDestinationType(), t -> {
							try {
								return converter.convert(t);
							} catch (final Exception e) {
								throw new ConversionException();
							}
						});
				outputConversionInfos.add(outputConversionInfo);
			}
			// TODO: handle 'null' raw types
			// TODO: type-safety
			final Class<O> outputType = (Class<O>) m_funcInstance.member("output").member().getRawType();
			ObjectToDataCells<O> outputConversion;
			try {
				outputConversion = new ObjectToDataCells<>(outputType, outputConversionInfos);
			} catch (final ValidityException e) {
				throw new InvalidSettingsException(e);
			}
			DataRow inRow = null;
			while ((inRow = input.poll()) != null) {
				final I inConverted = m_inputConversion.apply(inRow);
				final O out = m_funcInstance.object().apply(inConverted);
				final DataCell[] outConverted = outputConversion.apply(out);
				output.push(new DefaultRow(inRow.getKey(), outConverted));
			}
		} finally {
			input.close();
			output.close();
		}
	}

	private static final class MemberToDataCellConversionInfoHelper<I> {

		private final String m_memberName;

		private final JavaToDataCellConverterFactory<I> m_factory;

		MemberToDataCellConversionInfoHelper(final String memberName, final JavaToDataCellConverterFactory<I> factory) {
			m_memberName = memberName;
			m_factory = factory;
		}
	}
}
