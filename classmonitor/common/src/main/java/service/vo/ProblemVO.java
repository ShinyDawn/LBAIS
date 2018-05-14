package service.vo;

/**
 * Created by elva on 2018/5/14.
 */
public class ProblemVO {
    private String title;
    private String detail;
    //-1 退步 0正常 1 进步
    private int isProgress;

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public int getIsProgress() {
        return isProgress;
    }

    public void setIsProgress(int isProgress) {
        this.isProgress = isProgress;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }
}
