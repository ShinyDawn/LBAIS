package service.service;

import service.model.InitInfo;
import service.model.StudentPose;
import service.repository.BehaviorRepository;
import service.repository.StudentRepository;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

/**
 * Created by elva on 2018/5/16.
 */
public interface AbsenteeService {
    public int analyseAbsentee(List<List<Point2D>> input, InitInfo initInfo);
}
