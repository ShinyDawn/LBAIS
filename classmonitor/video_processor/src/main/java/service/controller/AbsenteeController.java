package service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import service.model.InitInfo;
import service.service.AbsenteeService;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Created by elva on 2018/5/19.
 */
@Controller
public class AbsenteeController {
    @Autowired
    private AbsenteeService service;

    public int analyseAbsentee(List<List<Point2D>> input, InitInfo initInfo){
        return service.analyseAbsentee(input,initInfo);
    }
}
