package service.vo;

/**
 * Created by elva on 2018/4/30.
 */
public class DisciplineVO {
    private String date;
    private String discover_time;
    private String action;
    private int total_time;
    private String status;

    public DisciplineVO(String date, String discover_time, String action, int total_time, String status) {
        this.date = date;
        this.action = action;
        this.discover_time = discover_time;
        this.total_time = total_time;
        this.status = status;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }

    public int getTotal_time() {
        return total_time;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiscover_time() {
        return discover_time;
    }

    public void setDiscover_time(String discover_time) {
        this.discover_time = discover_time;
    }
}
