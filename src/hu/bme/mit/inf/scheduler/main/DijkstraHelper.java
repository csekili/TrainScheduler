package hu.bme.mit.inf.scheduler.main;

public class DijkstraHelper {
	public double weight = -1;
	public int fromNodeID;
	public int nodeID;

	public DijkstraHelper(double weight, int fromRouteID, int nodeID) {
		this.weight = weight;
		this.fromNodeID = fromRouteID;
		this.nodeID = nodeID;
	}

	public void setNewValues(double totalNewWeight, int fromNodeID) {
		if (weight == -1 || totalNewWeight < weight) {
			weight = totalNewWeight;
			this.fromNodeID = fromNodeID;
		}
	}

}
