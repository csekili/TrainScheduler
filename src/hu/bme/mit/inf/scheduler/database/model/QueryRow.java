package hu.bme.mit.inf.scheduler.database.model;

import java.util.ArrayList;

import hu.bme.mit.inf.scheduler.database.exc.ColumnNotFoundException;

public class QueryRow {
	private ArrayList<QueryCell> rowData;

	public QueryRow(ArrayList<QueryCell> rowData) {
		this.rowData = rowData;
	}

	public int getSize() {
		return rowData.size();
	}

	public ArrayList<QueryCell> getData() {
		return rowData;
	}

	public QueryCell getColumn(int index) {
		return rowData.get(index - 1);
	}

	public QueryCell getColumn(String columnName) throws ColumnNotFoundException {
		for (QueryCell c : rowData) {
			if (c.getColumn().getColumnName().equalsIgnoreCase(columnName)) {
				return c;
			}
		}
		throw new ColumnNotFoundException(columnName);
	}
}
