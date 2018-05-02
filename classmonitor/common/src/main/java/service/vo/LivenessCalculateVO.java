package service.vo;

/**
 * Created by elva on 2018/5/2.
 */
public class LivenessCalculateVO {
    private String date;
    private String Subject;
    //个人举手次数
    private double timesOfHandsUp;
    //提问次数
    private double timesOfAllHandsUp;
    //发呆时长
    private double timeOfDull;
    //缺勤时长
    private double timeOfAbsentee;
    //课时长
    private double timeOfLesson;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public double getTimeOfAbsentee() {
        return timeOfAbsentee;
    }

    public double getTimeOfDull() {
        return timeOfDull;
    }

    public double getTimeOfLesson() {
        return timeOfLesson;
    }

    public double getTimesOfAllHandsUp() {
        return timesOfAllHandsUp;
    }

    public double getTimesOfHandsUp() {
        return timesOfHandsUp;
    }

    public void setTimeOfAbsentee(double timeOfAbsentee) {
        this.timeOfAbsentee = timeOfAbsentee;
    }

    public void setTimeOfDull(double timeOfDull) {
        this.timeOfDull = timeOfDull;
    }

    public void setTimeOfLesson(double timeOfLesson) {
        this.timeOfLesson = timeOfLesson;
    }

    public void setTimesOfAllHandsUp(double timesOfAllHandsUp) {
        this.timesOfAllHandsUp = timesOfAllHandsUp;
    }

    public void setTimesOfHandsUp(double timesOfHandsUp) {
        this.timesOfHandsUp = timesOfHandsUp;
    }
}
