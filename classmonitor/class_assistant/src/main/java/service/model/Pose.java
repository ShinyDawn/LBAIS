package service.model;

public class Pose {

	private int cid;
	private int sid;
	private int raiseHand;
	private int stand;
	private double focus;
	private double x;
	private double y;
	private double angle;
	private boolean dected;

	public Pose() {
		cid = 0;
		sid = 1;
		raiseHand = 0;
		focus = 0;
		x = -1;
		y = -1;
		stand = 0;
	}

	public boolean isDected() {
		return dected;
	}

	public void setDected(boolean dected) {
		this.dected = dected;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double avg) {
		this.angle = avg;
	}

	public int getStand() {
		return stand;
	}

	public void setStand(int stand) {
		this.stand = stand;
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

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getRaiseHand() {
		return raiseHand;
	}

	public void setRaiseHand(int raiseHand) {
		this.raiseHand = raiseHand;
	}

	public double getFocus() {
		return focus;
	}

	public void setFocus(double focus) {
		this.focus = focus;
	}
}
