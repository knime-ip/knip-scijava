package org.knime.knip.scijava.commands.adapter.basic;

import java.io.IOException;

import net.imagej.ImgPlus;
import net.imglib2.img.Img;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.knip.base.data.img.ImgPlusCell;
import org.knime.knip.base.data.img.ImgPlusCellFactory;
import org.knime.knip.scijava.commands.KnimeExecutionService;
import org.knime.knip.scijava.commands.adapter.OutputAdapterPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Adapter for Img to {@link ImgPlusCell}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = OutputAdapterPlugin.class)
public class ImgOutputAdapter implements
		OutputAdapterPlugin<Img, ImgPlusCell> {

	@Parameter
	KnimeExecutionService execService;

	@Override
	public DataCell createCell(Img o) {
		try {
			return new ImgPlusCellFactory(execService.getExecutionContext())
					.createCell(new ImgPlus(o));
		} catch (IOException e) {
			// TODO 
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<Img> getSourceType() {
		return Img.class;
	}

	@Override
	public DataType getDataCellType() {
		return ImgPlusCell.TYPE;
	}

}
