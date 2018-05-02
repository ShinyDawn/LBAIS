package service.vo;

import javax.persistence.Entity;

/**
 * Created by elva on 2018/5/2.
 */
public class LivenessDataVO {
    private String date;
    private String subject;
    private double data;

    public LivenessDataVO(String date,String subject,double data){
        this.data=data;
        this.date=date;
        this.subject=subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }
}
