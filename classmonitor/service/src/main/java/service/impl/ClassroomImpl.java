package service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.dao.Classroom;
import service.repository.ClassroomRepository;
import service.service.ClassroomService;

@Service
public class ClassroomImpl implements ClassroomService{
	@Autowired
	private ClassroomRepository classroom;

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
}
