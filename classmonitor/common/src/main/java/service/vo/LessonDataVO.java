package service.vo;

import javax.persistence.Entity;

/**
 * Created by elva on 2018/5/2.
 */
public class LessonDataVO {

    private String field;
    private int integer;

    private double data;

    public LessonDataVO(String field, int integer){
        this.field=field;
        this.integer = integer;
    }

    public int getInteger() {
        return integer;
    }

    public String getField() {
        return field;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public void setField(String field) {
        this.field = field;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }


}
