package hu.bme.mit.inf.scheduler.model;

import java.util.ArrayList;

public class ScheduleEntry {
	private Train train;
	private ArrayList<ScheduleSection> sections;
	private ArrayList<RailRoadElement> railRoadElements;
	private RailRoadElement from_station, to_station;

	public ScheduleEntry(Train train, ArrayList<ScheduleSection> sections, RailRoadElement from_station,
			RailRoadElement to_station) {
		this.train = train;
		this.sections = sections;
		this.from_station = from_station;
		this.to_station = to_station;
		setSegments();
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

	private void setSegments() {
		railRoadElements = new ArrayList<>();
		for (int i = 0; i < sections.size(); i++) {
			ScheduleSection s = sections.get(i);
			railRoadElements.add(s.getRoute().getFrom());
			if (i == sections.size() - 1) {
				railRoadElements.add(s.getRoute().getTo());
			}
		}
		
	}
}
