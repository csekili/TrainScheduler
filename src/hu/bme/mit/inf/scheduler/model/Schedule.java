package hu.bme.mit.inf.scheduler.model;

import java.util.ArrayList;

public class Schedule {
	private ArrayList<ScheduleEntry> entries;

	public Schedule() {
		this.entries = new ArrayList<>();
	}

	public ScheduleEntry getEntry(int index) {
		return entries.get(index);
	}

	public ArrayList<ScheduleEntry> getEntriesFrom(Segment station) {
		return getEntries(station, true);
	}

	public ArrayList<ScheduleEntry> getEntriesTo(Segment station) {
		return getEntries(station, false);
	}

	/**
	 * 
	 * @param station
	 *            given station
	 * @param from
	 *            true=entries from given station, false= entries to given station
	 * @return
	 */
	private ArrayList<ScheduleEntry> getEntries(Segment station, boolean from) {
		ArrayList<ScheduleEntry> data = new ArrayList<>();
		for (ScheduleEntry s : entries) {
			if (from && s.getFrom_station().getId() == station.getId()) {
				data.add(s);
			} else if (!from && s.getTo_station().getId() == station.getId()) {
				data.add(s);
			}
		}
		return data;
	}

	public void addScheduleEntry(ScheduleEntry entry) {
		//TODO tobb utvonalra ez nem mukodik
		entries.clear();
		entries.add(entry);
	}
}
