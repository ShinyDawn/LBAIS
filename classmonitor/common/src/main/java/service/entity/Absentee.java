package service.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by elva on 2018/4/30.
 */
@Entity
public class Absentee {
    @Id
    @GeneratedValue

    //请假表
    private int id;
    //class id
    private int cid;
    //student id in class
    private int sid;
    //date absent; Format:YYYY-MM-DD
    private String date;
    //time period absent
    private int tid;
    //具体理由
    private String reason;
    //病假或者事假
    private String type;

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int getCid() {
        return cid;
    }

    public int getId() {
        return id;
    }

    public int getSid() {
        return sid;
    }

    public int getTid() {
        return tid;
    }

    public String getReason() {
        return reason;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
