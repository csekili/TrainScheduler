package hu.bme.mit.inf.scheduler.database.exc;

public class ColumnNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public ColumnNotFoundException() {
		super();
	}

	public ColumnNotFoundException(String colName) {
		super("Column '" + colName + "' not found in query result.");
	}
}
