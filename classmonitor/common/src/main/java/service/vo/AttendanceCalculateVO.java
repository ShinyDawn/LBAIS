package service.vo;

/**
 * Created by elva on 2018/5/1.
 */
public class AttendanceCalculateVO {
/**
    //迟到算0.5课时（不管有无请假）
    double lateForClass = 0;
    //早退算0.5课时（不管有无请假）
    double earlyOut = 0;
    //缺勤请假算1课时；自习缺勤请假不算入课时
    double absentee = 0;
    //缺勤旷课算半天的课时（假设每天的课时相等）；自习旷课算1课时
    double cuttingSchool=0;
 */
    private int timesOfLate;
    private int timesOfEarly;
    private int timesOfAbsentee;
    private int timesOfCutting;

    public int getTimesOfAbsentee() {
        return timesOfAbsentee;
    }

    public int getTimesOfCutting() {
        return timesOfCutting;
    }

    public int getTimesOfEarly() {
        return timesOfEarly;
    }

    public int getTimesOfLate() {
        return timesOfLate;
    }

    public void setTimesOfAbsentee(int timesOfAbsentee) {
        this.timesOfAbsentee = timesOfAbsentee;
    }

    public void setTimesOfCutting(int timesOfCutting) {
        this.timesOfCutting = timesOfCutting;
    }

    public void setTimesOfEarly(int timesOfEarly) {
        this.timesOfEarly = timesOfEarly;
    }

    public void setTimesOfLate(int timesOfLate) {
        this.timesOfLate = timesOfLate;
    }

}
