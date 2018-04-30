package service.vo;

/**
 * Created by elva on 2018/4/30.
 */
public class AttendanceVO {
    private String date;
    private int tid;
    private String subject;
    private String behavior;
    private String status;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getStatus() {
        return status;
    }

    public String getBehavior() {
        return behavior;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }
}
