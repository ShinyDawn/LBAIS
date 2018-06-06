package service.entity.study;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//编号
//班级
//日期
//描述
//是否处理


@Entity
public class Pattern {
	@Id
	@GeneratedValue
	private int id;
	private int class_id;
	private int date;
	private String destribute;
	private int isHandle;//0 no;1 yes;2  误报
	
	
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
}
