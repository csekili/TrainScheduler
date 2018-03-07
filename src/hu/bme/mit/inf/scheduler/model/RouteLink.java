package hu.bme.mit.inf.scheduler.model;

public class RouteLink {
	private RailRoadElement viaNode;
	private Route fromRoute, toRoute;
	public String s;

	public RouteLink(RailRoadElement viaNode, Route fromRoute, Route toRoute, String s) {
		this.viaNode = viaNode;
		this.fromRoute = fromRoute;
		this.toRoute = toRoute;
		this.s = s;
	}

	public RailRoadElement getViaNode() {
		return viaNode;
	}

	public Route getFromRoute() {
		return fromRoute;
	}

	public Route getToRoute() {
		return toRoute;
	}
}
