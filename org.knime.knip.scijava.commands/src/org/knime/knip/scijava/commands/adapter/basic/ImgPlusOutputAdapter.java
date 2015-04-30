package org.knime.knip.scijava.commands.adapter.basic;

import java.io.IOException;

import net.imagej.ImgPlus;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.def.BooleanCell;
import org.knime.knip.base.data.img.ImgPlusCell;
import org.knime.knip.base.data.img.ImgPlusCellFactory;
import org.knime.knip.scijava.commands.KnimeExecutionService;
import org.knime.knip.scijava.commands.adapter.OutputAdapterPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for ImgPlus to {@link ImgPlusCell}.
 * 
 * @author Jonathan Hale (	University of Konstanz)
 * 
 */
@Plugin(type = OutputAdapterPlugin.class)
public class ImgPlusOutputAdapter implements
		OutputAdapterPlugin<ImgPlus, ImgPlusCell> {

	@Parameter
	KnimeExecutionService execService;


	@Override
	public DataCell createCell(ImgPlus o) {
		try {
			return new ImgPlusCellFactory(execService.getExecutionContext()).createCell(o);
		} catch (IOException e) {
			// TODO 
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<ImgPlus> getSourceType() {
		return ImgPlus.class;
	}

	@Override
	public DataType getDataCellType() {
		return ImgPlusCell.TYPE;
	}

}
