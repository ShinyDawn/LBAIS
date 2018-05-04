package service.vo;

/**
 * Created by elva on 2018/4/30.
 */
public class StudentInfoVO {
    private int sid;
    private String name;
    private String problem;
    private double attendanceRate;
    private double livenessRate;
    private double deciplineRate;


    public void setLivenessRate(double livenessRate) {
        this.livenessRate = livenessRate;
    }

    public double getLivenessRate() {
        return livenessRate;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public double getAttendanceRate() {
        return attendanceRate;
    }

    public double getDeciplineRate() {
        return deciplineRate;
    }

    public String getName() {
        return name;
    }

    public String getProblem() {
        return problem;
    }

    public void setAttendanceRate(double attendanceRate) {
        this.attendanceRate = attendanceRate;
    }

    public void setDeciplineRate(double deciplineRate) {
        this.deciplineRate = deciplineRate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}
