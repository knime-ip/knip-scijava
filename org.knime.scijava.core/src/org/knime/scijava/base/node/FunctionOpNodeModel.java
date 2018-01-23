package org.knime.scijava.base.node;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.streamable.InputPortRole;
import org.knime.core.node.streamable.OutputPortRole;
import org.knime.core.node.streamable.PartitionInfo;
import org.knime.core.node.streamable.PortInput;
import org.knime.core.node.streamable.PortOutput;
import org.knime.core.node.streamable.RowInput;
import org.knime.core.node.streamable.RowOutput;
import org.knime.core.node.streamable.StreamableOperator;
import org.scijava.ops2.FunctionOp;
import org.scijava.struct2.Struct;
import org.scijava.struct2.StructInstance;

public class FunctionOpNodeModel<I, O> extends NodeModel {

	private NodeStructInstance<?> m_structInstance;

	private StructInstance<FunctionOp<I, O>> m_func;

	private DataRowToObject<I> m_rowToObject;

	private ObjectToDataCells<O> m_objectToCells;

	protected FunctionOpNodeModel(Struct struct, StructInstance<FunctionOp<I, O>> func) {
		super(1, 1);
		m_structInstance = new NodeStructInstance<>(struct);
		m_func = func;
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
		return super.configure(inSpecs);
	}

	@Override
	protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
		return super.execute(inData, exec);
	}

	@Override
	public StreamableOperator createStreamableOperator(PartitionInfo partitionInfo, PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {
		return new StreamableOperator() {

			@Override
			public void runFinal(PortInput[] inputs, PortOutput[] outputs, ExecutionContext exec) throws Exception {
				RowInput input = (RowInput) inputs[0];
				RowOutput output = (RowOutput) outputs[0];
				DataRow inRow = null;
				while ((inRow = input.poll()) != null) {
					I inConverted = m_rowToObject.apply(inRow);
					O out = m_func.object().apply(inConverted);
					DataCell[] outConverted = m_objectToCells.apply(out);
					output.push(new DefaultRow(inRow.getKey(), outConverted));
				}
			}
		};
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		try {
			m_structInstance.saveSettingsTo(settings);
		} catch (InvalidSettingsException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		m_structInstance.loadSettingsFrom(settings);

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
	public InputPortRole[] getInputPortRoles() {
		return new InputPortRole[] { InputPortRole.DISTRIBUTED_STREAMABLE };
	}

	@Override
	public OutputPortRole[] getOutputPortRoles() {
		return new OutputPortRole[] { OutputPortRole.DISTRIBUTED };
	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	@Override
	protected void reset() {
	}
}
