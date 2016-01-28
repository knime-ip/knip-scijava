package org.knime.scijava.core;

import org.scijava.command.CommandInfo;
import org.scijava.command.CommandModule;
import org.scijava.module.MethodCallException;
import org.scijava.module.ModuleException;

public class KnimeSciJavaModule extends CommandModule {

	private boolean flag = false;

	public KnimeSciJavaModule(CommandInfo info) throws ModuleException {
		super(info);
	}

	@Override
	public void initialize() throws MethodCallException {
		// only initialize once
		if (!flag) {
			super.initialize();
			flag = true;
		}
	}

}
