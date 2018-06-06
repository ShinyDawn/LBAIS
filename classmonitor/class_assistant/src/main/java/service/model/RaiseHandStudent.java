package service.model;

public class RaiseHandStudent {

	private int cid;
	private int sid;
	private String sname;
	private int raiseHand;
	private int standup;
	private int answer;

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public int getAnswer() {
		return answer;
	}

	public void setAnswer(int answer) {
		this.answer = answer;
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

	public int getStandup() {
		return standup;
	}

	public void setStandup(int standup) {
		this.standup = standup;
	}
}
