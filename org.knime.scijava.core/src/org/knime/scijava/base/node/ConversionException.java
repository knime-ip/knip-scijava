package org.knime.scijava.base.node;

public final class ConversionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConversionException() {
	}

	public ConversionException(final String message) {
		super(message);
	}

	public ConversionException(final Throwable cause) {
		super(cause);
	}

	public ConversionException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ConversionException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
