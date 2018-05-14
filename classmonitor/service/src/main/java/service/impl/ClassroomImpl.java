package service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.entity.Classroom;
import service.repository.ClassroomRepository;
import service.service.ClassroomService;
import service.vo.StudentInfoVO;

@Service
public class ClassroomImpl implements ClassroomService {
    @Autowired
    private ClassroomRepository classroom;

    public List<Classroom> findAllClassesByteacher(String uid) {


        List<Classroom> classrooms = classroom.findClassroomsByTeacher(uid);
        List<Classroom> temp = new ArrayList<>();

        if (classrooms == null || classrooms.isEmpty()) {
            return temp;
        }

        return classrooms;
    }

    @Override
    public List<Classroom> findAllClasses() {
        return classroom.findAllClasses();
    }

    @Override
    public void updateOne(int id, String cname, String teacher, int num, String grade, String date) {
        classroom.updateById(id, cname, teacher, num, grade, date);
    }

    @Override
    public void deleteOne(int id) {
        classroom.deleteById(id);
    }

    @Override
    public void addOne(String cname, String teacher, int num, String grade, String date) {
        Classroom c = new Classroom();
        c.setCname(cname);
        c.setDate(date);
        c.setGrade(grade);
        c.setNum(num);
        c.setTeacher(teacher);
        classroom.save(c);
    }

    @Override
    public Classroom getInfo(int id) {
        return classroom.findById(id);
    }

    public List<StudentInfoVO> getStudentInfoList(int cid) {
        return null;
    }

    ;

    public List<StudentInfoVO> getStudentInfoBySimpleFilter(int cid, String problem) {
        return null;
    }

    ;

    public List<StudentInfoVO> getStudentInfoByAdvancedFilter(int cid) {
        return null;
    }

    ;

    public double getClassLivenessRate(int cid, String date) {
        return 0;
    }

    ;

    public double getClassAttendenceRate(int cid, String date) {
        return 0;
    }

    ;

    public int getClassEvent(int cid, String date) {
        return 0;
    }

    ;

    public void leaveForApproval(int cid, int sid, String date, int tid, int reason) {

    }

    ;
}
