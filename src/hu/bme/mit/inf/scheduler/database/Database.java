package hu.bme.mit.inf.scheduler.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
	private String host;
	private int port;
	private String user;
	private String pass;
	private String database;
	private Connection conn;

	public Database(String host, int port, String user, String pass, String database) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
		this.database = database;
	}

	private void start() {
		if (conn != null)
			stop();
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://" + host + ":" + port + "/" + database + "?" + "user=" + user + "&password=" + pass);
		} catch (SQLException e) {
			e.printStackTrace();
			stop();
		} finally {
		}
	}

	private void stop() {
		if (conn == null)
			return;
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean isConnected() {
		if (conn == null)
			return false;
		try {
			return !conn.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ResultSet selectStatement(String query, String... params) {
		if (!isConnected())
			start();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(query);
			if (params.length > 0) {
				int i = 1;
				for (String p : params) {
					stmt.setString(i, p);
					i++;
				}
			}
			ResultSet rs = stmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int runQuery(String query, String... params) {
		if (!isConnected())
			start();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(query);
			if (params.length > 0) {
				int i = 1;
				for (String p : params) {
					stmt.setString(i, p);
					i++;
				}
			}
			return stmt.executeUpdate();
		} catch (SQLException e) {
			return -1;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
