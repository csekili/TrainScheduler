package hu.bme.mit.inf.scheduler.database.exc;

public class ParameterException extends Exception {
	private static final long serialVersionUID = 1L;

	public ParameterException() {
		super();
	}

	public ParameterException(String colName) {
		super("Column '" + colName + "' not found in query result.");
	}
}
