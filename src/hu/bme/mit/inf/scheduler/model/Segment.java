package hu.bme.mit.inf.scheduler.model;

public class Segment extends RailRoadElement {

	private RailRoadElement connectedTo1, connectedTo2;
	private boolean enabled, station;

	public Segment(int id, int connectedTo1, int connectedTo2, boolean station) {
		this(id, new RailRoadElement(connectedTo1), new RailRoadElement(connectedTo2), station);
	}

	public Segment(int id, RailRoadElement connectedTo1, RailRoadElement connectedTo2, boolean station) {
		super(id);
		this.connectedTo1 = connectedTo1;
		this.connectedTo2 = connectedTo2;
		this.enabled = true;
		this.station = station;
	}

	public RailRoadElement getConnectedTo1() {
		return connectedTo1;
	}

	public void setConnectedTo1(RailRoadElement connectedTo1) {
		this.connectedTo1 = connectedTo1;
	}

	public RailRoadElement getConnectedTo2() {
		return connectedTo2;
	}

	public void setConnectedTo2(RailRoadElement connectedTo2) {
		this.connectedTo2 = connectedTo2;
	}

	public boolean isStation() {
		return station;
	}

	public void setStation(boolean isStation) {
		this.station = isStation;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.enabled = isEnabled;
	}

}
