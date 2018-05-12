package service.vo;

/**
 * Created by elva on 2018/5/11.
 */
public class ApprovalVO {
    int id;
    private String date;
    private String name;
    private int courseTime;
    private String course;
    private String type;
    private String reason;
    private int sid;

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getSid() {
        return sid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public int getCourseTime() {
        return courseTime;
    }

    public String getCourse() {
        return course;
    }

    public void setCourseTime(int courseTime) {
        this.courseTime = courseTime;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
