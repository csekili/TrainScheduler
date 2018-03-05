package hu.bme.mit.inf.scheduler.database.model;

import java.util.ArrayList;

import hu.bme.mit.inf.scheduler.database.exc.ColumnNotFoundException;
import hu.bme.mit.inf.scheduler.database.exc.ParameterException;
import hu.bme.mit.inf.scheduler.database.exc.UndefinedDataException;

public class QueryResult {
	private ArrayList<QueryColumn> columns;
	private ArrayList<QueryRow> data;
	private int rows = 0;

	public QueryResult() {
		columns = new ArrayList<>();
		data = new ArrayList<>();
	}

	public void addColumn(QueryColumn c) throws UndefinedDataException {
		if (rows <= 0)
			columns.add(c);
		else
			throw new UndefinedDataException("Cannot add new columns if rows exist in query result.");
	}

	public void addRow(QueryRow row) throws ParameterException {
		if (columns.size() != row.getData().size())
			throw new ParameterException("Number of columns not correct. [table rowcount] != [param rowcount]: "
					+ columns.size() + " != " + row.getData().size());
		data.add(row);
		rows++;
	}

	public QueryColumn getColumnMetaData(int index) {
		return columns.get(index - 1);
	}

	public ArrayList<QueryColumn> getAllColumnMetaData() {
		return columns;
	}

	public QueryColumn getColumnMetaData(String columnName) throws ColumnNotFoundException {
		for (QueryColumn c : columns) {
			if (c.getColumnName().equalsIgnoreCase(columnName))
				return c;
		}
		throw new ColumnNotFoundException(columnName);
	}

	public QueryRow getRow(int index) throws ParameterException {
		if (index <= 0 || index > rows) {
			throw new ParameterException();
		}

		return data.get(index - 1);
	}

	public int getRowCount() {
		return rows;
	}

	public int getColumnCount() {
		return columns.size();
	}
}
