package hu.bme.mit.inf.scheduler.main;

import hu.bme.mit.inf.scheduler.model.Route;
import hu.bme.mit.inf.scheduler.model.RouteLink;

public class DijkstraHelper {
	public double weight = -1;
	public RouteLink fromRouteLink;
	public Route node, fromNode;

	public DijkstraHelper(double weight, Route node) {
		this.weight = weight;
		this.node = node;
	}

	public void setNewValues(double totalNewWeight, RouteLink fromRouteLink) {
		if (weight == -1 || totalNewWeight < weight) {
			weight = totalNewWeight;
			this.fromRouteLink = fromRouteLink;
			this.fromNode = fromRouteLink.getFromRoute();
		}
	}

}
