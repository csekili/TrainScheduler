package hu.bme.mit.inf.scheduler.model;

import java.util.ArrayList;

import hu.bme.mit.inf.scheduler.database.exc.ParameterException;

public class Route {
	private ArrayList<Path> paths;
	private RailRoadElement from, to;
	private boolean enabled;
	public double weight = 1;

	public Route(Route r) {
		this(r.paths);
	}

	public Route(ArrayList<Path> paths) {
		try {
			setPaths(paths);
			setEnabled(true);
		} catch (ParameterException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Path> getPaths() {
		return paths;
	}

	private void setPaths(ArrayList<Path> paths) throws ParameterException {
		setFrom(paths.get(0).getFrom());
		setTo(paths.get(paths.size() - 1).getTo());
		for (int i = 0; i < paths.size() - 1; i++) {
			if (paths.get(i).getTo().getId() != paths.get(i + 1).getVia().getId()) {
				throw new ParameterException("Not contiguous paths from station/turnout to stationt/turnout.");
			}
		}
		this.paths = paths;
	}

	public RailRoadElement getFrom() {
		return from;
	}

	private void setFrom(RailRoadElement from) {
		this.from = from;
	}

	public RailRoadElement getTo() {
		return to;
	}

	public void setTo(RailRoadElement to) {
		this.to = to;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
