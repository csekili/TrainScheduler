package hu.bme.mit.inf.scheduler.model;

import java.util.Date;

public class ScheduleSection {
	private Route route;
	private Date supposedDeparture, actualDeparture;

	public ScheduleSection(Route route, Date supposedDeparture, Date actualDeparture) {
		this.route = route;
		this.supposedDeparture = supposedDeparture;
		this.actualDeparture = actualDeparture;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Date getSupposedDeparture() {
		return supposedDeparture;
	}

	public void setSupposedDeparture(Date supposedDeparture) {
		this.supposedDeparture = supposedDeparture;
	}

	public Date getActualDeparture() {
		return actualDeparture;
	}

	public void setActualDeparture(Date actualDeparture) {
		this.actualDeparture = actualDeparture;
	}
}
