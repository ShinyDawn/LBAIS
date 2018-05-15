package service.entity.study;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//报警信息处理
//处理编号
//学生编号
//处理类别


@Entity
@Table( name = "alarmhandle" )
public class AlarmHandle {
	@Id
//	@GeneratedValue
	private String hid;
	private int aid;
	private int sid;
	private int type_id;
	
	public String getHid() {
		return hid;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
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
