package service.vo;

import java.util.List;

/**
 * Created by elva on 2018/5/14.
 */
public class LivenessShowVO {
    private String subject;
    private List<LivenessVO> data;

    public void setData(List<LivenessVO> data) {
        this.data = data;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public List<LivenessVO> getData() {
        return data;
    }
}
