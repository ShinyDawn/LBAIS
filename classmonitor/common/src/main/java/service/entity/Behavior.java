package service.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by elva on 2018/4/30.
 */
@Entity
public class Behavior {
    @Id
    @GeneratedValue
    private int id;
    //date when the behavior is detected
    private String date;
    //time when the behavior is detected
    private String start;
    //for the behavior last for certain time; could be the same as start;
    private String end;
    //class id
    private int cid;
    //student id in class
    private int sid;
    //for example: late for school;talk
    private String action;
    //leave approved or unsolved or solved
    private String status;
    //redundant: time transfered to lesson time
    private int tid;
    //redundant: the lesson name; help to classify wheather happened in a study or a lesson
    private String place;
    //time that behavior lasts(min)
    private int total_time;

    public void setTid(int tid) {
        this.tid = tid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getTid() {
        return tid;
    }

    public int getSid() {
        return sid;
    }

    public int getId() {
        return id;
    }

    public int getCid() {
        return cid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnd() {
        return end;
    }

    public String getPlace() {
        return place;
    }

    public String getStart() {
        return start;
    }

    public String getStatus() {
        return status;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal_time() {
        return total_time;
    }

    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
}

