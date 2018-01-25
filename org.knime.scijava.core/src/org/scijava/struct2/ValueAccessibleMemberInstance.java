package org.scijava.struct2;

import java.lang.reflect.Type;

import org.apache.commons.lang3.ClassUtils;
import org.scijava.util2.Types;

public class ValueAccessibleMemberInstance<T> implements MemberInstance<T> {

	private final Member<T> member;
	private final ValueAccessible<T> access;
	private final Object object;

	public <M extends Member<T> & ValueAccessible<T>> ValueAccessibleMemberInstance(final M member,
			final Object object) {
		this.member = member;
		this.access = member;
		this.object = object;
	}

	@Override
	public Member<T> member() {
		return member;
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isWritable() {
		return true;
	}

	@Override
	public T get() {
		return access.get(object);
	}

	@Override
	public void set(final Object value) {
		// HACK: convert primitive type to wrapper type to ensure assignability (which should be legal here)
		Type type = member().getType();
		if (type instanceof Class) {
			type = ClassUtils.primitiveToWrapper((Class<?>) type);
		}
		if (!Types.isAssignable(value.getClass(), type)) {
			throw new IllegalArgumentException("value of type " + //
					Types.name(value.getClass()) + " is not assignable to " + //
					Types.name(member().getRawType()));
		}
		@SuppressWarnings("unchecked")
		final T tValue = (T) value;
		access.set(tValue, object);
	}
}
