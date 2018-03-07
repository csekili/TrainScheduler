package hu.bme.mit.inf.scheduler.main;

import hu.bme.mit.inf.scheduler.model.RailRoadElement;
import hu.bme.mit.inf.scheduler.model.Route;
import hu.bme.mit.inf.scheduler.model.TurnOut;

public class DijkstraHelper {
	public double weight = -1;
	public Route route;
	public RailRoadElement node, fromNode;

	public DijkstraHelper(double weight, RailRoadElement node) {
		this.weight = weight;
		this.node = node;
	}

	public void setNewValues(double totalNewWeight, Route route, Route fromRoute) {
		if(route.getFrom() instanceof TurnOut) {
			if(node == ((TurnOut)route.getFrom()).getDivergent() || node ==((TurnOut)route.getFrom()).getStraight()) {
			}
		}
		if (weight == -1 || totalNewWeight < weight) {
			weight = totalNewWeight;
			this.fromNode = route.getFrom();
		}
	}

}
