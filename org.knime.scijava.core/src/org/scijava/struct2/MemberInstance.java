package org.scijava.struct2;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface MemberInstance<T> extends Supplier<T> {

	Member<T> member();

	default boolean isReadable() {
		return false;
	}

	default boolean isWritable() {
		return false;
	}

	/**
	 * Get's the value of the member.
	 * 
	 * @return The value of the {@link Member} with the given key.
	 * @throws UnsupportedOperationException
	 *             if the member is not readable (see {@link #isReadable()}).
	 */
	@Override
	default T get() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the value of the member.
	 * 
	 * @param value
	 *            The value to set.
	 * @throws UnsupportedOperationException
	 *             if the member is not writable (see {@link #isWritable()}).
	 */
	default void set(Object value) {
		throw new UnsupportedOperationException();
	}

	default void addChangeListener(BiConsumer<MemberInstance<T>, T> listener) {
		throw new UnsupportedOperationException();
	}

	default void removeChangeListener(BiConsumer<MemberInstance<T>, T> listener) {
		throw new UnsupportedOperationException();
	}
}
