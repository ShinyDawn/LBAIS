package service.vo;

/**
 * Created by elva on 2018/4/30.
 */
public class LivenessVO {
    private String Date;
    private String subject;
    private int tid;
    //课堂参与度
    private double livenessRate;
    //课堂专注度
    private double concentrationRate;
    //课堂举手次数
    private double handsUpTimes;
    //课堂综合表现
    private double generalRate;

    public double getGeneralRate() {
        return generalRate;
    }

    public void setGeneralRate(double generalRate) {
        this.generalRate = generalRate;
    }

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

    public void setGeneralRate() {
        this.generalRate = this.concentrationRate * 0.5 + this.livenessRate * 0.5;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }
}
