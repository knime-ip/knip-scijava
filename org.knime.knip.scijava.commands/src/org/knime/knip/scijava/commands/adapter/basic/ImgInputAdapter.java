package org.knime.knip.scijava.commands.adapter.basic;

import net.imagej.ImgPlus;
import net.imglib2.img.Img;

import org.knime.knip.base.data.img.ImgPlusValue;
import org.knime.knip.scijava.commands.adapter.InputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link ImgPlusValue} to Img.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapterPlugin.class)
public class ImgInputAdapter implements
		InputAdapterPlugin<ImgPlusValue, Img> {

	@Override
	public Img getValue(ImgPlusValue v) {
		return v.getImgPlus();
	}

	@Override
	public Class<ImgPlusValue> getDataValueType() {
		return ImgPlusValue.class;
	}

	@Override
	public Class<Img> getType() {
		return Img.class;
	}

}
