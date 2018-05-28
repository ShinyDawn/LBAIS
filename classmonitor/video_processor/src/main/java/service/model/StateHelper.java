package service.model;

public class StateHelper {
	private int cid;
	private int sid;
	private int start_seq;
	private String state;
	private String description;
	private int beh_id;
	
	public StateHelper() {
		super();
		this.cid = 0;
		this.sid = 0;
		this.start_seq = 0;
		this.state = "坐";
		this.description="正常";
		this.beh_id = 0;
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

	public int getStart_seq() {
		return start_seq;
	}

	public void setStart_seq(int start_seq) {
		this.start_seq = start_seq;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getBeh_id() {
		return beh_id;
	}

	public void setBeh_id(int beh_id) {
		this.beh_id = beh_id;
	}
	
	
	
	
	
}
