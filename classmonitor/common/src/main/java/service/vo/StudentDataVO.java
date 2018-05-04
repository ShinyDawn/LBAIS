package service.vo;

/**
 * Created by elva on 2018/5/4.
 */
public class StudentDataVO {

    private int sid;
    private double data;

    public StudentDataVO(int sid, double data) {
        this.sid = sid;
        this.data = data;
    }

    public void setData(double data) {
        this.data = data;
    }

    public double getData() {
        return data;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getSid() {
        return sid;
    }
}
