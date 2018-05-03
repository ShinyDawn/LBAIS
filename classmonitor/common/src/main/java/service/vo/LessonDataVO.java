package service.vo;

import javax.persistence.Entity;

/**
 * Created by elva on 2018/5/2.
 */
public class LessonDataVO {

    //以下三个参数确定一堂课 每天第几课时哪一堂课
    private String date;
    private String subject;
    private int tid;

    private double data;

    public LessonDataVO(String date, int tid){
        this.date=date;
        this.tid = tid;
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

    public void setTid(int tid) {
        this.tid = tid;
    }
    public int getTid() {
        return tid;
    }
}
