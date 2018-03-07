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
	}

	/// In-Events

	public void pathChanged(Path path) {

	}

	public void segmentChanged(RailRoadElement section) {

	}

}