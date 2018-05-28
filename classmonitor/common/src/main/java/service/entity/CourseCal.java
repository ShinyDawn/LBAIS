package service.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by elva on 2018/5/28.
 */
@Entity
public class CourseCal {
    @Id
    @GeneratedValue
    private int id;
    private int cid;
    private int sid;
    private String date;
    private String subject;
    private int tid;
    private double livenessRate;
    private double concentrationRate;
    private double generalRate;

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getSid() {
        return sid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getCid() {
        return cid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getTid() {
        return tid;
    }

    public String getDate() {
        return date;
    }

    public double getConcentrationRate() {
        return concentrationRate;
    }

    public void setConcentrationRate(double concentrationRate) {
        this.concentrationRate = concentrationRate;
    }

    public double getGeneralRate() {
        return generalRate;
    }

    public void setGeneralRate(double generalRate) {
        this.generalRate = generalRate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getLivenessRate() {
        return livenessRate;
    }

    public void setLivenessRate(double livenessRate) {
        this.livenessRate = livenessRate;
    }
}
