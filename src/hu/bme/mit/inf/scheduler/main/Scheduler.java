package hu.bme.mit.inf.scheduler.main;

import java.util.ArrayList;

import hu.bme.mit.inf.scheduler.database.Calculations;
import hu.bme.mit.inf.scheduler.database.DatabaseQueries;
import hu.bme.mit.inf.scheduler.model.Path;
import hu.bme.mit.inf.scheduler.model.RailRoadElement;
import hu.bme.mit.inf.scheduler.model.Route;
import hu.bme.mit.inf.scheduler.model.Schedule;
import hu.bme.mit.inf.scheduler.model.Segment;
import hu.bme.mit.inf.scheduler.model.Train;
import hu.bme.mit.inf.scheduler.model.TurnOut;

public class Scheduler {
	private ArrayList<TurnOut> turnouts;
	private ArrayList<Segment> segments;

	private ArrayList<Route> availableRoutes;
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

		if (availableRoutes != null)
			availableRoutes.clear();
		availableRoutes = Calculations.getRoutes(paths, sections);

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
		// Dijkstra algoritmus, all weight=1

		ArrayList<RailRoadElement> nodes = Calculations.getRouteBorders(Calculations.getSections(turnouts, segments));

		ArrayList<DijkstraHelper> helper = new ArrayList<>();
		for (int i = 0; i < nodes.size(); i++) {
			helper.add(new DijkstraHelper(-1, fromStation.getId(), nodes.get(i).getId()));
		}

		RailRoadElement actualNode = null;
		for (int i = 0; i < nodes.size(); i++) {
			if (i == 0) {
				actualNode = fromStation;
			} else {
				actualNode = getMinWeight(helper, nodes);
			}
			if (actualNode == null)
				continue;
			ArrayList<Route> routes = getRoutesFrom(actualNode);
			updateHelper(helper, routes, actualNode);
		}
	}

	/**
	 * Dijkstra update
	 * 
	 * @param helper
	 * @param routes
	 * @param actualNode
	 * @param lastNode
	 */
	private void updateHelper(ArrayList<DijkstraHelper> helper, ArrayList<Route> routes, RailRoadElement actualNode) {
		DijkstraHelper actualNodesHelper = null;
		for (DijkstraHelper d : helper) {
			if (d.nodeID == actualNode.getId()) {
				actualNodesHelper = d;
				break;
			}
		}
		double nodeWeigt = actualNodesHelper.weight == -1 ? 0 : actualNodesHelper.weight;

		for (Route r : routes) {
			double newWeight = nodeWeigt + r.weight;
			for (DijkstraHelper d : helper) {
				if (d.nodeID == r.getTo().getId()) {
					d.setNewValues(newWeight, actualNode.getId());
					break;
				}
			}
		}
	}

	private RailRoadElement getMinWeight(ArrayList<DijkstraHelper> helper, ArrayList<RailRoadElement> nodes) {
		double minWeight = helper.get(0).weight;
		int actualNodeID = helper.get(0).nodeID;
		for (DijkstraHelper d : helper) {
			if (minWeight > d.weight) {
				minWeight = d.weight;
				actualNodeID = d.nodeID;
			}
		}
		for (RailRoadElement r : nodes) {
			if (r.getId() == actualNodeID) {
				return r;
			}
		}
		return null;
	}

	private ArrayList<Route> getRoutesFrom(RailRoadElement fromElement) {
		ArrayList<Route> data = new ArrayList<>();
		for (Route r : availableRoutes) {
			if (!r.isEnabled())
				continue;
			if (r.getFrom().getId() == fromElement.getId())
				data.add(r);
		}
		return data;
	}

	/// In-Events

	public void pathChanged(Path path) {

	}

	public void segmentChanged(RailRoadElement section) {

	}

}