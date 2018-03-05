package hu.bme.mit.inf.scheduler.database.model;

public class QueryCell {
	private QueryColumn column;
	private String data;

	public QueryCell(QueryColumn column, String data) {
		this.column = column;
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public QueryColumn getColumn() {
		return column;
	}

}
