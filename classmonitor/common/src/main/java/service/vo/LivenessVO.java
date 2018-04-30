package service.vo;

/**
 * Created by elva on 2018/4/30.
 */
public class LivenessVO {
    private String Date;
    private String subject;
    private double livenessRate;
    private double concentrationRate;
    private double handsUpTimes;

    public void setDate(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public double getConcentrationRate() {
        return concentrationRate;
    }

    public double getHandsUpTimes() {
        return handsUpTimes;
    }

    public double getLivenessRate() {
        return livenessRate;
    }

    public String getSubject() {
        return subject;
    }

    public void setConcentrationRate(double concentrationRate) {
        this.concentrationRate = concentrationRate;
    }

    public void setHandsUpTimes(double handsUpTimes) {
        this.handsUpTimes = handsUpTimes;
    }

    public void setLivenessRate(double livenessRate) {
        this.livenessRate = livenessRate;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
