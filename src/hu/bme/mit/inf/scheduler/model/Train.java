package hu.bme.mit.inf.scheduler.model;

public class Train {
	private double x, y;
	private int id;

	public Train(Train t) {
		this(t.x, t.y, t.id);
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Train(double x, double y, int id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}

}
