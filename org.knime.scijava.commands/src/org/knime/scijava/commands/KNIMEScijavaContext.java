package org.knime.scijava.commands;

import org.knime.scijava.commands.adapter.InputAdapterService;
import org.knime.scijava.commands.adapter.OutputAdapterService;
import org.knime.scijava.commands.io.InputDataRowService;
import org.knime.scijava.commands.io.OutputDataRowService;
import org.knime.scijava.commands.mapping.ColumnToInputMappingService;
import org.knime.scijava.commands.mapping.OutputToColumnMappingService;
import org.knime.scijava.commands.settings.NodeDialogSettingsService;
import org.knime.scijava.commands.settings.NodeModelSettingsService;
import org.knime.scijava.commands.settings.SettingsModelTypeService;
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
	 * @return the {@link InputDataRowService} in this {@link Context}
	 */
	public InputDataRowService input();

	/**
	 * @return the {@link OutputDataRowService} in this {@link Context}
	 */
	public OutputDataRowService output();

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
	 * @return the {@link NodeDialogSettingsService} in this {@link Context}
	 */
	public NodeDialogSettingsService nodeDialogSettings();

	/**
	 * @return the {@link NodeModelSettingsService} in this {@link Context}
	 */
	public NodeModelSettingsService nodeModelSettings();
	/**
	 * @return the {@link KNIMEExecutionService} in this {@link Context}
	 */
	public KNIMEExecutionService execution();

}
