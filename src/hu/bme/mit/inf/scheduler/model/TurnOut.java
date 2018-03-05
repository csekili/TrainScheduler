package hu.bme.mit.inf.scheduler.model;

public class TurnOut extends RailRoadElement {
	private RailRoadElement top, straight, divergent;
	private double height, width, x, y;

	public TurnOut(int id, int top, int straight, int divergent, double height, double width, double x, double y) {
		this(id, new RailRoadElement(top), new RailRoadElement(straight), new RailRoadElement(divergent), height, width,
				x, y);
	}

	public TurnOut(int id, RailRoadElement top, RailRoadElement straight, RailRoadElement divergent, double height,
			double width, double x, double y) {
		super(id);
		this.top = top;
		this.straight = straight;
		this.divergent = divergent;
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
	}

	public RailRoadElement getTop() {
		return top;
	}

	public void setTop(RailRoadElement top) {
		this.top = top;
	}

	public RailRoadElement getStraight() {
		return straight;
	}

	public void setStraight(RailRoadElement straight) {
		this.straight = straight;
	}

	public RailRoadElement getDivergent() {
		return divergent;
	}

	public void setDivergent(RailRoadElement divergent) {
		this.divergent = divergent;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
