package service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.dao.Student;
import service.repository.StudentRepository;
import service.service.StudentService;
import service.vo.AttendanceVO;
import service.vo.DisciplineVO;
import service.vo.LivenessVO;

@Service
public class StudentImpl implements StudentService {
	@Autowired
	StudentRepository studentRepo;

	public List<Student> getInfo(int cid) {
		return studentRepo.findByCid(cid);
	}

	public int add(int cid, int sid, String name, String gender, int x, int y) {
		int seat = studentRepo.countSeat(cid, sid, x, y);
		if (seat != 0)
			return -2;
		Student current = studentRepo.findBySid(sid);
		if (current == null) {
			Student s = new Student();
			s.setCid(cid);
			s.setSid(sid);
			s.setName(name);
			s.setGender(gender);
			s.setX(x);
			s.setY(y);
			current = studentRepo.save(s);
			return current.getId();
		} else
			return -1;
	}

	public int update(int id, int cid, int sid, String name, String gender, int x, int y) {
		int seat = studentRepo.countSeat(cid, sid, x, y);
		if (seat != 0)
			return -2;
		int num = studentRepo.countSid(cid, sid);
		if (num == 1) {
			studentRepo.updateOne(id, sid, name, gender, x, y);
			return 1;
		} else
			return -1;
	}

	public void delete(int id) {
		studentRepo.deleteOne(id);
	}

	//part of Personal Model
	public List<AttendanceVO> getAttendanceInfo(int cid, int sid, int period){
		return null;
	};

	public double getAttendenceRate(int cid,int sid,int period){
		return 0;
	};

	public List<LivenessVO> geLivenessInfo(int cid, int sid, int period, String subject){
		return null;
	};
	public double getLivenessRate(int cid,int sid,int period,String subject){
		return 0;
	};

	public List<DisciplineVO> getDisplineInfo(int cid, int sid, int period){
		return null;
	};
	public double getDisciplineRate(int cid,int sid,int period){
		return 0;
	};
}
