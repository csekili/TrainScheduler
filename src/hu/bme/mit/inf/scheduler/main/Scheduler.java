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
import hu.bme.mit.inf.scheduler.model.ScheduleSection;
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
		ArrayList<Route> startingRoutes = getAvailableRoutesFromStation(fromStation, routes);
		ArrayList<Route> destinationRoutes = getAvailableRoutesToStation(toStation, routes);

		ArrayList<ScheduleEntry> possibleEntries = new ArrayList<>();

		for (Route startRoute : startingRoutes) {
			possibleEntries.add(shortestRoute(routes, startRoute, destinationRoutes));
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

	private ScheduleEntry shortestRoute(ArrayList<Route> routes, Route startRoute, ArrayList<Route> destinationRoutes) {
		ArrayList<DijkstraHelper> helpers = new ArrayList<>();
		for (Route r : routes) {
			helpers.add(new DijkstraHelper(-1, r));
		}

		//
		Route actualNode = startRoute;
		DijkstraHelper actualHelper = getDijkstraHelperOfNode(startRoute, helpers);
		actualHelper.weight = 0;
		for (int i = 0; i <= routes.size(); i++) {
			if (i > 0)
				actualNode = getMinWeight(helpers);
			actualHelper = getDijkstraHelperOfNode(actualNode, helpers);
			ArrayList<RouteLink> neighbours = getNeighbours(routes, actualNode);
			for (RouteLink rl : neighbours) {
				DijkstraHelper helper = getDijkstraHelperOfNode(rl.getToRoute(), helpers);
				if (helper == null)
					continue;
				helper.setNewValues(actualHelper.weight + rl.getToRoute().weight, rl);
			}
		}

		//

		DijkstraHelper minhelper = getDijkstraHelperOfNode(destinationRoutes.get(0), helpers);
		for (int i = 1; i < destinationRoutes.size(); i++) {
			Route r = destinationRoutes.get(i);
			DijkstraHelper helper = getDijkstraHelperOfNode(r, helpers);
			if (minhelper.weight == -1) {
				minhelper = helper;
				continue;
			}

			if (helper.weight != -1 && helper.weight < minhelper.weight)
				minhelper = helper;
		}

		//
		ArrayList<ScheduleSection> sections = new ArrayList<>();

		RouteLink lastRouteLink = minhelper.fromRouteLink;
		sections.add(new ScheduleSection(lastRouteLink.getFromRoute(), null, null));
		while (true) {
			if (lastRouteLink.getFromRoute() == startRoute)
				break;
			lastRouteLink = getDijkstraHelperOfNode(lastRouteLink.getFromRoute(), helpers).fromRouteLink;
			sections.add(new ScheduleSection(lastRouteLink.getFromRoute(), null, null));
		}

		ScheduleEntry solution = new ScheduleEntry(null, sections, (Segment) startRoute.getFrom(),
				(Segment) destinationRoutes.get(0).getTo());

		return solution;
	}

	private DijkstraHelper getDijkstraHelperOfNode(Route r, ArrayList<DijkstraHelper> helpers) {
		for (DijkstraHelper h : helpers) {
			if (h.node == r) {
				return h;
			}
		}
		return null;
	}

	private ArrayList<RouteLink> getNeighbours(ArrayList<Route> routes, Route route) {
		ArrayList<RouteLink> data = new ArrayList<>();
		for (RouteLink rl : availableRouteLinks) {
			if (rl.getFromRoute() == route) {
				data.add(rl);
			}
		}
		return data;
	}

	/// In-Events

	private Route getMinWeight(ArrayList<DijkstraHelper> helpers) {
		Route min = helpers.get(0).node;
		int minindex = 0;
		for (int i = 0; i < helpers.size(); i++) {
			DijkstraHelper h = helpers.get(i);
			if (h.weight < helpers.get(minindex).weight && h.weight != -1) {
				min = h.node;
				minindex = i;
			}
		}
		return min;
	}

	private ArrayList<Route> getAvailableRoutesToStation(Segment toStation, ArrayList<Route> routes) {
		ArrayList<Route> data = new ArrayList<>();
		for (Route r : routes)
			if (r.getTo().getId() == toStation.getId())
				data.add(r);
		return data;
	}

	private ArrayList<Route> getAvailableRoutesFromStation(Segment fromStation, ArrayList<Route> routes) {
		ArrayList<Route> data = new ArrayList<>();
		for (Route r : routes)
			if (r.getFrom().getId() == fromStation.getId())
				data.add(r);
		return data;
	}

	public void pathChanged(Path path) {

	}

	public void segmentChanged(RailRoadElement section) {

	}

}