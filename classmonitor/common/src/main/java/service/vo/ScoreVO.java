package service.vo;

public class ScoreVO {

	private int sid;
	private double avg;
	private String date;

	public ScoreVO(int sid, double avg, String date) {
		this.sid = sid;
		this.avg = avg;
		this.date = date;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
