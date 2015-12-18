package org.knime.scijava.commands;

import org.knime.scijava.commands.adapter.InputAdapterService;
import org.knime.scijava.commands.adapter.OutputAdapterService;
import org.knime.scijava.commands.io.InputDataRowService;
import org.knime.scijava.commands.io.OutputDataRowService;
import org.knime.scijava.commands.mapping.ColumnToInputMappingService;
import org.knime.scijava.commands.mapping.OutputToColumnMappingService;
import org.knime.scijava.commands.settings.NodeDialogSettingsService;
import org.knime.scijava.commands.settings.NodeModelSettingsService;
import org.knime.scijava.commands.settings.NodeSettingsService;
import org.knime.scijava.commands.settings.SettingsModelTypeService;
import org.scijava.Context;
import org.scijava.NullContextException;
import org.scijava.plugin.Parameter;

/**
 * Default implementation of {@link KNIMEScijavaContext}. Enables convenient
 * access to the KNIME related services in a Scijava Context.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
public class DefaultKNIMEScijavaContext implements KNIMEScijavaContext {

	// private final Logger m_log = LoggerFactory.getLogger(getClass());

	private Context m_context;

	@Parameter
	private InputAdapterService m_inputAdapterService;
	@Parameter
	private OutputAdapterService m_outputAdapterService;
	@Parameter
	private InputDataRowService m_inputRowService;
	@Parameter
	private OutputDataRowService m_outputRowService;
	@Parameter
	private ColumnToInputMappingService m_inputMappingService;
	@Parameter
	private OutputToColumnMappingService m_outputMappingService;
	@Parameter
	private SettingsModelTypeService m_settingsModelTypesService;
	@Parameter
	private NodeSettingsService m_nodeSettingsService;
	@Parameter
	private KNIMEExecutionService m_executionService;
	@Parameter
	private NodeModelSettingsService m_nodeModelService;
	@Parameter
	private NodeDialogSettingsService m_nodeDialogService;

	@Override
	public Context context() {
		if (m_context == null)
			throw new NullContextException();
		return m_context;
	}

	@Override
	public Context getContext() {
		return m_context;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException
	 *             If a service is missing
	 */
	@Override
	public void setContext(final Context context)
			throws IllegalArgumentException {
		if (m_context != null) {
			throw new IllegalArgumentException(
					"CODING PROBLEM - Scijava context set mutiple times. Should only be set once.");
		}
		m_context = context;
		context.inject(this);
	}

	@Override
	public InputAdapterService inputAdapters() {
		return m_inputAdapterService;
	}

	@Override
	public OutputAdapterService outputAdapters() {
		return m_outputAdapterService;
	}

	@Override
	public InputDataRowService input() {
		return m_inputRowService;
	}

	@Override
	public OutputDataRowService output() {
		return m_outputRowService;
	}

	@Override
	public ColumnToInputMappingService inputMapping() {
		return m_inputMappingService;
	}

	@Override
	public OutputToColumnMappingService outputMapping() {
		return m_outputMappingService;
	}

	@Override
	public SettingsModelTypeService settingsModelTypes() {
		return m_settingsModelTypesService;
	}

	@Override
	public KNIMEExecutionService execution() {
		return m_executionService;
	}

	@Override
	public NodeDialogSettingsService nodeDialogSettings() {
		return m_nodeDialogService;
	}

	@Override
	public NodeModelSettingsService nodeModelSettings() {
		return m_nodeModelService;
	}

}
