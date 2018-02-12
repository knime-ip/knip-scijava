package org.knime.scijava.base.node;

import org.scijava.struct2.Member;
import org.scijava.struct2.Struct;

public class DefaultNodeStructInstance<C> extends NodeStructInstance<C> {

	public DefaultNodeStructInstance(final Struct struct, final C object) {
		super(struct, object);
	}

	@Override
	NodeMemberInstance<?> createMemberInstance(final Member<?> member, final Object c) {
		return new NodeMemberInstance<>(member, c);
	}
}
