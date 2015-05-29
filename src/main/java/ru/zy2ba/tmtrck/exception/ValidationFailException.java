package ru.zy2ba.tmtrck.exception;

/**
 * 
 * @author zy2ba
 * @since 04.05.2015.
 * 
 * Runtime Exception handler for Business Validations.
 */
public class ValidationFailException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidationFailException() {
		super();
	}

	public ValidationFailException(String message) {
		super(message);
	}

	public ValidationFailException(Throwable throwable) {
		super(throwable);
	}

	public ValidationFailException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
