package service.vo.study;

import service.entity.study.Alarm;

public class AlarmVO {
	private int id;
	private int class_id;
	private String date;
	private String time;
	private String destribute;
	private int isHandle;//0 no;1 yes;2  误报
	private String handle_id;
	private String vedio_path;
	
	public AlarmVO(Alarm temp) {
		this.id=temp.getId();
		this.class_id=temp.getClass_id();
		String day=Integer.toString(temp.getDate());
		this.date=day.substring(0,4)+"-"+day.substring(4,6)+"-"+day.substring(6);
		this.time=temp.getTime();
		this.destribute=temp.getDestribute();
		this.isHandle=temp.getIsHandle();
		this.handle_id=temp.getHandle_id();
		this.vedio_path=temp.getVedio_path();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDestribute() {
		return destribute;
	}
	public void setDestribute(String destribute) {
		this.destribute = destribute;
	}
	public int getIsHandle() {
		return isHandle;
	}
	public void setIsHandle(int isHandle) {
		this.isHandle = isHandle;
	}
	public String getHandle_id() {
		return handle_id;
	}
	public void setHandle_id(String handle_id) {
		this.handle_id = handle_id;
	}
	
	public String getVedio_path() {
		return vedio_path;
	}
	public void setVedio_path(String vedio_path) {
		this.vedio_path = vedio_path;
	}
	
}
