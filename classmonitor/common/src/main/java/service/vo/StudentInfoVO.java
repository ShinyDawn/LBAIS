package service.vo;

import java.util.List;

/**
 * Created by elva on 2018/4/30.
 */
public class StudentInfoVO {
    private int sid;
    private String name;
    private List<ProblemVO> problem;
    private double attendanceRate;
    private double livenessRate;
    private double deciplineRate;
    private double generalRate;
    private double concentrationRate;

    public void setGeneralRate(double generalRate) {
        this.generalRate = generalRate;
    }

    public double getGeneralRate() {
        return generalRate;
    }

    public void setConcentrationRate(double concentrationRate) {
        this.concentrationRate = concentrationRate;
    }

    public double getConcentrationRate() {
        return concentrationRate;
    }

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

    public void setProblem(List<ProblemVO> problem) {
        this.problem = problem;
    }

    public List<ProblemVO> getProblem() {
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

}
