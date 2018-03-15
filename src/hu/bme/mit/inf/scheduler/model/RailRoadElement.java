package hu.bme.mit.inf.scheduler.model;

public class RailRoadElement {
	private int id;

	public boolean equals(RailRoadElement r) {
		return r.id == id;
	}
	
	public RailRoadElement(int id) {
		this.setId(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
