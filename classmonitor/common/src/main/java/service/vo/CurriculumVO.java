package service.vo;

public class CurriculumVO {

	private int tid;
	private String time;
	private int day;
	private String course;

	public CurriculumVO(int tid, String time, int day, String course) {
		this.tid = tid;
		this.time = time;
		this.day = day;
		this.course = course;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}
}
