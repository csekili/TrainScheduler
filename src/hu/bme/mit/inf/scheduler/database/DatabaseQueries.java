package hu.bme.mit.inf.scheduler.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import hu.bme.mit.inf.scheduler.config.Config;
import hu.bme.mit.inf.scheduler.database.Calculations;
import hu.bme.mit.inf.scheduler.database.Database;
import hu.bme.mit.inf.scheduler.model.Path;
import hu.bme.mit.inf.scheduler.model.RailRoadElement;
import hu.bme.mit.inf.scheduler.model.Route;
import hu.bme.mit.inf.scheduler.model.RouteLink;
import hu.bme.mit.inf.scheduler.model.Segment;
import hu.bme.mit.inf.scheduler.model.TurnOut;

public class DatabaseQueries implements Config {
	public static ArrayList<RouteLink> getRouteLinks() {
		ArrayList<Route> routes = getRoutes();
		return Calculations.getRouteLinks(routes);
	}

	public static ArrayList<Segment> getStations() {
		ArrayList<RailRoadElement> sections = getSections();
		return Calculations.getStations(sections);
	}

	public static ArrayList<Route> getRoutes() {
		ArrayList<Path> paths = getPaths();
		ArrayList<RailRoadElement> sections = getSections();
		return Calculations.getRoutes(paths, sections);
	}

	public static ArrayList<Path> getPaths() {
		ArrayList<RailRoadElement> sections = getSections();
		return Calculations.getPaths(sections);
	}

	public static ArrayList<RailRoadElement> getSections() {
		ArrayList<TurnOut> turnouts = getTurnouts();
		ArrayList<Segment> segments = getSegments();
		return Calculations.getSections(turnouts, segments);
	}

	public static ArrayList<TurnOut> getTurnouts() {
		Database dc = new Database(DB_HOST, DB_PORT, DB_USER, DB_PASS, DB_NAME);
		String query = "select * from turnout;";
		ResultSet rs = dc.selectStatement(query);

		ArrayList<TurnOut> turnouts = new ArrayList<>();

		try {
			while (rs.next()) {
				int id = rs.getInt("tid");
				int top = rs.getInt("top");
				int straight = rs.getInt("straight");
				int divergent = rs.getInt("divergent");
				double height = rs.getDouble("height");
				double width = rs.getDouble("width");
				double x = rs.getDouble("x");
				double y = rs.getDouble("y");
				turnouts.add(new TurnOut(id, top, straight, divergent, height, width, x, y));
			}

			rs.close();
			dc.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return turnouts;
	}

	public static ArrayList<Segment> getSegments() {
		Database dc = new Database(DB_HOST, DB_PORT, DB_USER, DB_PASS, DB_NAME);
		String query = "select * from segment;";
		ResultSet rs = dc.selectStatement(query);

		ArrayList<Segment> segments = new ArrayList<>();

		try {
			while (rs.next()) {
				int id = rs.getInt("sid");
				int connectedTo1 = rs.getInt("connectedTo_1");
				int connectedTo2 = rs.getInt("connectedTo_2");
				boolean station = rs.getInt("isStation") != 0;
				segments.add(new Segment(id, connectedTo1, connectedTo2, station));
			}

			rs.close();
			dc.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return segments;
	}
}
