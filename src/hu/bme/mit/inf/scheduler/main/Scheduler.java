package hu.bme.mit.inf.scheduler.main;

import java.util.ArrayList;

import hu.bme.mit.inf.scheduler.database.Calculations;
import hu.bme.mit.inf.scheduler.database.DatabaseQueries;
import hu.bme.mit.inf.scheduler.model.Path;
import hu.bme.mit.inf.scheduler.model.RailRoadElement;
import hu.bme.mit.inf.scheduler.model.Route;
import hu.bme.mit.inf.scheduler.model.RouteLink;
import hu.bme.mit.inf.scheduler.model.Schedule;
import hu.bme.mit.inf.scheduler.model.ScheduleEntry;
import hu.bme.mit.inf.scheduler.model.Segment;
import hu.bme.mit.inf.scheduler.model.Train;
import hu.bme.mit.inf.scheduler.model.TurnOut;

public class Scheduler {
	private ArrayList<TurnOut> turnouts;
	private ArrayList<Segment> segments;

	private ArrayList<RouteLink> availableRouteLinks;
	private Schedule schedules;
	private ArrayList<Segment> stations;

	public void loadData() {
		turnouts = DatabaseQueries.getTurnouts();
		segments = DatabaseQueries.getSegments();

		reCalcData();
		setSchedules(new Schedule());
	}

	private void reCalcData() {
		ArrayList<RailRoadElement> sections = Calculations.getSections(turnouts, segments);
		ArrayList<Path> paths = Calculations.getPaths(sections);

		if (availableRouteLinks != null)
			availableRouteLinks.clear();
		availableRouteLinks = Calculations.getRouteLinks(Calculations.getRoutes(paths, sections));

		if (stations != null)
			stations.clear();
		stations = Calculations.getStations(sections);

	}

	public Schedule getSchedules() {
		return schedules;
	}

	private void setSchedules(Schedule schedules) {
		this.schedules = schedules;
	}

	public void addSchedule(Train t, Segment fromStation, Segment toStation) {
		ArrayList<RailRoadElement> sections = Calculations.getSections(turnouts, segments);
		ArrayList<Path> paths = Calculations.getPaths(sections);
		ArrayList<Route> routes = Calculations.getRoutes(paths, sections);
		ArrayList<RouteLink> routeLinks = Calculations.getRouteLinks(routes);
		ArrayList<Route> startingRoutes = Calculations.getAvailableRoutesFromStation(fromStation, routes);
		ArrayList<Route> destinationRoutes = Calculations.getAvailableRoutesToStation(toStation, routes);

		ArrayList<ScheduleEntry> possibleEntries = new ArrayList<>();

		for (Route startRoute : startingRoutes) {
			possibleEntries.add(Calculations.shortestRoute(t, routes, startRoute, destinationRoutes, routeLinks));
		}

		double minw = possibleEntries.get(0).weight();
		int mini = 0;
		for (int i = 1; i < possibleEntries.size(); i++) {
			ScheduleEntry e = possibleEntries.get(i);
			if (e.weight() < minw) {
				minw = e.weight();
				mini = i;
			}
		}
		
		schedules.addScheduleEntry(possibleEntries.get(mini));
	}

	public ArrayList<Segment> getStations(){
		return stations;
	}
	
	/// In-Events

	public void pathChanged(Path path) {

	}

	public void segmentChanged(RailRoadElement section) {

	}

}