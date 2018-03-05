package hu.bme.mit.inf.scheduler.database.exc;

public class UndefinedDataException extends Exception {
	private static final long serialVersionUID = 1L;

	public UndefinedDataException() {
		super();
	}

	public UndefinedDataException(String msg) {
		super(msg);
	}
}
