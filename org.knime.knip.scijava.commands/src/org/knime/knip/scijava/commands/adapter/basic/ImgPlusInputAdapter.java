package org.knime.knip.scijava.commands.adapter.basic;

import net.imagej.ImgPlus;

import org.knime.knip.base.data.img.ImgPlusValue;
import org.knime.knip.scijava.commands.adapter.InputAdapterPlugin;
import org.scijava.plugin.Plugin;

/**
 * Adapter for {@link ImgPlusValue} to ImgPlus.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * 
 */
@Plugin(type = InputAdapterPlugin.class)
public class ImgPlusInputAdapter implements
		InputAdapterPlugin<ImgPlusValue, ImgPlus> {

	@Override
	public ImgPlus getValue(ImgPlusValue v) {
		return v.getImgPlus();
	}

	@Override
	public Class<ImgPlusValue> getDataValueType() {
		return ImgPlusValue.class;
	}

	@Override
	public Class<ImgPlus> getType() {
		return ImgPlus.class;
	}

}
