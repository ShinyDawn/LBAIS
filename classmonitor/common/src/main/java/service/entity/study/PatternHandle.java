package service.entity.study;

//模式信息处理
//处理编号
//学生编号
//处理类别

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PatternHandle {
	@Id
//	@GeneratedValue
	private String hid;
	private int pid;
	private int sid;
	private int type_id;
	
	public String getHid() {
		return hid;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
}
