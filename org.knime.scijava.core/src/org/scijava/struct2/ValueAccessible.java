package org.scijava.struct2;


public interface ValueAccessible<T> {

	T get(Object o);

	void set(T value, Object o);

}
