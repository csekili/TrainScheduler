package hu.bme.mit.inf.scheduler.database.model;

public class QueryColumn {
	private int columnIndex;
	private String columnName;
	private int columnType; // java.sql.Types

	public QueryColumn(int columnIndex, String columnName, int columnType) {
		this.columnIndex = columnIndex;
		this.columnName = columnName;
		this.columnType = columnType;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public String getColumnName() {
		return columnName;
	}

	public int getColumnType() {
		return columnType;
	}

}
