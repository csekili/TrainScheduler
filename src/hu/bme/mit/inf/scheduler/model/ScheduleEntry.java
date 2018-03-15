package hu.bme.mit.inf.scheduler.model;

import java.util.ArrayList;

public class ScheduleEntry {
	private Train train;
	private ArrayList<ScheduleSection> sections;
	private RailRoadElement from_station, to_station;

	private ArrayList<RailRoadElement> railRoadElements_RouteBorders;
	private ArrayList<RailRoadElement> railRoadElements;
	private ArrayList<Tuple> tuples;
	private ArrayList<Route> routes;

	public ScheduleEntry(Train train, ArrayList<ScheduleSection> sections, RailRoadElement from_station,
			RailRoadElement to_station) {
		this.train = train;
		this.sections = sections;
		this.from_station = from_station;
		this.to_station = to_station;
		setSegments();
		setTuples();
	}

	private void setTuples() {
		tuples = new ArrayList<>();
		for (int i = 0; i < railRoadElements.size(); i++) {
			if (i < railRoadElements.size() - 2) {
				RailRoadElement from = railRoadElements.get(i);
				RailRoadElement via = railRoadElements.get(i + 1);
				RailRoadElement to = railRoadElements.get(i + 2);
				tuples.add(new Tuple(from, via, to));
			} else {
				RailRoadElement from = railRoadElements.get(i);
				if (i == railRoadElements.size() - 2) {
					RailRoadElement via = railRoadElements.get(i + 1);
					tuples.add(new Tuple(from, via, null));
				} else if (i == railRoadElements.size() - 1) {
					tuples.add(new Tuple(from, null, null));
				}
			}
		}
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public ArrayList<ScheduleSection> getSections() {
		return sections;
	}

	public void setSections(ArrayList<ScheduleSection> sections) {
		this.sections = sections;
	}

	public RailRoadElement getFrom_station() {
		return from_station;
	}

	public void setFrom_station(Segment from_station) {
		this.from_station = from_station;
	}

	public RailRoadElement getTo_station() {
		return to_station;
	}

	public void setTo_station(Segment to_station) {
		this.to_station = to_station;
	}

	public double weight() {
		double w = 0;
		for (ScheduleSection s : sections) {
			w += s.getRoute().weight;
		}
		return w;
	}

	public ArrayList<RailRoadElement> getRailRoadElements() {
		return railRoadElements;
	}

	public ArrayList<RailRoadElement> getRailRoadElements_RouteBorders() {
		return railRoadElements_RouteBorders;
	}

	public ArrayList<Tuple> getTuples() {
		return tuples;
	}

	private void setSegments() {
		railRoadElements_RouteBorders = new ArrayList<>();
		for (int i = 0; i < sections.size(); i++) {
			ScheduleSection s = sections.get(i);
			railRoadElements_RouteBorders.add(s.getRoute().getFrom());
			if (i == sections.size() - 1) {
				railRoadElements_RouteBorders.add(s.getRoute().getTo());
			}
		}

		routes = new ArrayList<>();
		for (int i = 0; i < sections.size(); i++) {
			ScheduleSection s = sections.get(i);
			routes.add(s.getRoute());
		}

		railRoadElements = new ArrayList<>();

		for (Route r : routes) {
			for (Path p : r.getPaths()) {
				addRailRoadElementToList(p.getFrom());
				addRailRoadElementToList(p.getVia());
				if (!r.getTo().equals(p.getVia()))
					addRailRoadElementToList(p.getTo());
			}
		}
	}

	private void addRailRoadElementToList(RailRoadElement r) {
		if (railRoadElements == null)
			railRoadElements = new ArrayList<>();

		for (RailRoadElement rre : railRoadElements) {
			if (rre.equals(r))
				return;
		}
		railRoadElements.add(r);
	}
}
