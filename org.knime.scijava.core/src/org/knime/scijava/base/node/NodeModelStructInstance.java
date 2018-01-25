package org.knime.scijava.base.node;

import org.scijava.struct2.Member;
import org.scijava.struct2.Struct;

public class NodeModelStructInstance<C> extends NodeStructInstance<C> {

	public NodeModelStructInstance(final Struct struct, final C object) {
		super(struct, object);
	}

	@Override
	NodeMemberInstance<?> createMemberInstance(final Member<?> member) {
		return new NodeMemberInstance<>(member);
	}
}
