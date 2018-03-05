package hu.bme.mit.inf.scheduler.model;

public class Path {
	private RailRoadElement to, from, via;

	private boolean enabled;

	public Path(Path p) {
		this(p.to, p.from, p.via);
	}

	public Path(RailRoadElement to, RailRoadElement from, RailRoadElement via) {
		setTo(to);
		setFrom(from);
		setVia(via);
		setEnabled(true);
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

	public RailRoadElement getVia() {
		return via;
	}

	public void setVia(RailRoadElement via) {
		this.via = via;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
