package org.knime.knip.scijava.commands;

import org.knime.knip.scijava.commands.adapter.InputAdapterService;
import org.knime.knip.scijava.commands.adapter.OutputAdapterService;
import org.knime.knip.scijava.commands.mapping.ColumnToInputMappingService;
import org.knime.knip.scijava.commands.mapping.OutputToColumnMappingService;
import org.knime.knip.scijava.commands.settings.NodeSettingsService;
import org.knime.knip.scijava.commands.settings.SettingsModelTypeService;
import org.scijava.Context;
import org.scijava.Contextual;

/**
 * Holds all contextual data of a KNIME Node.
 *
 * @author Jonathan Hale
 */
public interface KNIMEScijavaContext extends Contextual {

	/**
	 * @return the {@link InputAdapterService} in this {@link Context}
	 */
	public InputAdapterService inputAdapters();

	/**
	 * @return the {@link OutputAdapterService} in this {@link Context}
	 */
	public OutputAdapterService outputAdapters();

	/**
	 * @return the {@link KNIMEInputDataTableService} in this {@link Context}
	 */
	public KNIMEInputDataTableService inputTable();

	/**
	 * @return the {@link KNIMEOutputDataTableService} in this {@link Context}
	 */
	public KNIMEOutputDataTableService outputTable();

	/**
	 * @return the {@link ColumnToInputMappingService} in this {@link Context}
	 */
	public ColumnToInputMappingService inputMapping();

	/**
	 * @return the {@link OutputToColumnMappingService} in this {@link Context}
	 */
	public OutputToColumnMappingService outputMapping();

	/**
	 * @return the {@link SettingsModelTypeService} in this {@link Context}
	 */
	public SettingsModelTypeService settingsModelTypes();

	/**
	 * @return the {@link NodeSettingsService} in this {@link Context}
	 */
	public NodeSettingsService nodeSettings();

	/**
	 * @return the {@link KNIMEExecutionService} in this {@link Context}
	 */
	public KNIMEExecutionService execution();

}
