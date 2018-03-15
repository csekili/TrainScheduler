package hu.bme.mit.inf.scheduler.model;

public class Tuple {

	private RailRoadElement to, from, via;
	private boolean last_1, last_2;
	private boolean enabled;

	public Tuple(RailRoadElement from, RailRoadElement via, RailRoadElement to) {
		setTo(to);
		setFrom(from);
		setVia(via);
		setEnabled(true);
		last_2 = to == null && via == null;
		;
		last_1 = to == null && via != null;
	}

	public RailRoadElement getTo() {
		return to;
	}

	public void setTo(RailRoadElement to) {
		this.to = to;
	}

	public RailRoadElement getFrom() {
		return from;
	}

	public void setFrom(RailRoadElement from) {
		this.from = from;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isLast_2() {
		return last_2;
	}

	public boolean isLast_1() {
		return last_1;
	}

	public RailRoadElement getVia() {
		return via;
	}

	public void setVia(RailRoadElement via) {
		this.via = via;
	}

}
