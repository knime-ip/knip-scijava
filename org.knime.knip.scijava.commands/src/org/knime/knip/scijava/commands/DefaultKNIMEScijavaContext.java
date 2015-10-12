package org.knime.knip.scijava.commands;

import org.knime.knip.scijava.commands.adapter.InputAdapterService;
import org.knime.knip.scijava.commands.adapter.OutputAdapterService;
import org.knime.knip.scijava.commands.mapping.ColumnToInputMappingService;
import org.knime.knip.scijava.commands.mapping.OutputToColumnMappingService;
import org.knime.knip.scijava.commands.settings.NodeSettingsService;
import org.knime.knip.scijava.commands.settings.SettingsModelTypeService;
import org.scijava.Context;
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

	private Logger m_log = LoggerFactory.getLogger(getClass());

	@Parameter
	private Context m_context;

	@Parameter
	private InputAdapterService inputAdapterService;
	@Parameter
	private OutputAdapterService outputAdapterService;
	@Parameter
	private KNIMEInputDataTableService inputTableService;
	@Parameter
	private KNIMEOutputDataTableService outputTableService;
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

	@Override
	public Context context() {
		return m_context;
	}

	@Override
	public Context getContext() {
		return context();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IllegalArgumentException
	 *             If a service is missing
	 */
	@Override
	public void setContext(Context context) throws IllegalArgumentException {
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
	public KNIMEInputDataTableService inputTable() {
		return inputTableService;
	}

	@Override
	public KNIMEOutputDataTableService outputTable() {
		return outputTableService;
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
	public NodeSettingsService nodeSettings() {
		return nodeSettingsService;
	}

	@Override
	public KNIMEExecutionService execution() {
		return executionService;
	}

}
