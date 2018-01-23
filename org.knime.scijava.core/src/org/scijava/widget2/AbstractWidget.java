package org.scijava.widget2;

import org.scijava.struct2.MemberInstance;

public abstract class AbstractWidget implements Widget {

	private final MemberInstance<?> model;

	public AbstractWidget(final MemberInstance<?> model) {
		this.model = model;
		model.addChangeListener(this::modelChanged);
	}

	protected abstract void modelChanged(MemberInstance<?> source, Object oldValue);

	@Override
	public MemberInstance<?> model() {
		return model;
	}
}
