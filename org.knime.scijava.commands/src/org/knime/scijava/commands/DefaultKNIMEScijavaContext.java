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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link KNIMEScijavaContext}. Enables convenient
 * access to the KNIME related services in a Scijava Context.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
public class DefaultKNIMEScijavaContext implements KNIMEScijavaContext {

	private final Logger m_log = LoggerFactory.getLogger(getClass());

	@Parameter
	private Context m_context;

	@Parameter
	private InputAdapterService inputAdapterService;
	@Parameter
	private OutputAdapterService outputAdapterService;
	@Parameter
	private InputDataRowService inputRowService;
	@Parameter
	private OutputDataRowService outputRowService;
	@Parameter
	private ColumnToInputMappingService inputMappingService;
	@Parameter
	private OutputToColumnMappingService outputMappingService;
	@Parameter
	private SettingsModelTypeService settingsModelTypesService;
	@Parameter
	private NodeSettingsService nodeSettingsService;
	@Parameter
	private KNIMEExecutionService executionService;
	@Parameter
	private NodeModelSettingsService nodeModelService;
	@Parameter
	private NodeDialogSettingsService nodeDialogService;

	@Override
	public Context context() {
		if (m_context == null) throw new NullContextException();
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
		if (m_context == context) {
			m_log.warn(
					"CODING PROBLEM - Scijava context set mutiple times. Should only be set once.");
			return;
		}
		context.inject(this);
	}

	@Override
	public InputAdapterService inputAdapters() {
		return inputAdapterService;
	}

	@Override
	public OutputAdapterService outputAdapters() {
		return outputAdapterService;
	}

	@Override
	public InputDataRowService input() {
		return inputRowService;
	}

	@Override
	public OutputDataRowService output() {
		return outputRowService;
	}

	@Override
	public ColumnToInputMappingService inputMapping() {
		return inputMappingService;
	}

	@Override
	public OutputToColumnMappingService outputMapping() {
		return outputMappingService;
	}

	@Override
	public SettingsModelTypeService settingsModelTypes() {
		return settingsModelTypesService;
	}

	@Override
	public KNIMEExecutionService execution() {
		return executionService;
	}

	@Override
	public NodeDialogSettingsService nodeDialogSettings() {
		return nodeDialogService;
	}

	@Override
	public NodeModelSettingsService nodeModelSettings() {
		return nodeModelService;
	}

}
