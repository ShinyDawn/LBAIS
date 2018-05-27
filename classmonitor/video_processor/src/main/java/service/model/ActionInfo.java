package service.model;

/**
 * Created by elva on 2018/5/21.
 */
public class ActionInfo {
    private String name;
    private String start;

    public ActionInfo(String name,String start){
        this.name = name;
        this.start =start;
    }

    public ActionInfo(String name){
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStart() {
        return start;
    }
}
