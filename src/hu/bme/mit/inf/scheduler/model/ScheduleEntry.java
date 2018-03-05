package hu.bme.mit.inf.scheduler.model;

import java.util.ArrayList;

public class ScheduleEntry {
	private Train train;
	private ArrayList<ScheduleSection> sections;
	private Segment from_station, to_station;

	public ScheduleEntry(Train train, ArrayList<ScheduleSection> sections, Segment from_station, Segment to_station) {
		this.train = train;
		this.sections = sections;
		this.from_station = from_station;
		this.to_station = to_station;
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

	public Segment getFrom_station() {
		return from_station;
	}

	public void setFrom_station(Segment from_station) {
		this.from_station = from_station;
	}

	public Segment getTo_station() {
		return to_station;
	}

	public void setTo_station(Segment to_station) {
		this.to_station = to_station;
	}
}
