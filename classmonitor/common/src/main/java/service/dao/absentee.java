package service.dao;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by elva on 2018/4/30.
 */
public class absentee {
    @Id
    @GeneratedValue
    private int id;
    //class id
    private int cid;
    //student id in class
    private int sid;
    //date absent; Format:YYYY-MM-DD
    private String date;
    //time period absent
    private int tid;
    //illness or personal affairs
    private String reason;

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
}
