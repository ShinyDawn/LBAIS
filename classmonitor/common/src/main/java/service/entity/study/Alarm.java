package service.entity.study;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//报警信息:
//报警编号
//班级
//日期
//报警时间
//描述
//是否处理
//视频开始
//视频路径

@Entity
@Table( name = "alarm" )
public class Alarm {
	
	@Id
	@GeneratedValue
	private int id;
	private int class_id;
	private int date;
	private String time;
	private String destribute;
	private int isHandle;//0 待核实;1 待处理;2  误报;3 已处理
	private int start;
	private String vedio_path;
	
	
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
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
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
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	
	public String getVedio_path() {
		return vedio_path;
	}
	public void setVedio_path(String vedio_path) {
		this.vedio_path = vedio_path;
	}
	
}
