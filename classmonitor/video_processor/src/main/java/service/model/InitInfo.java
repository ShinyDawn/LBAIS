package service.model;

import java.util.List;
import java.util.Map;

/**
 * Created by elva on 2018/5/19.
 */
public class InitInfo {
    private int cid;
    private String date;
    private int tid;
    private String place;
    private Map<Integer, List> seatTable;

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getCid() {
        return cid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

    public Map<Integer, List> getSeatTable() {
        return seatTable;
    }

    public void setSeatTable(Map<Integer, List> seatTable) {
        this.seatTable = seatTable;
    }

}
