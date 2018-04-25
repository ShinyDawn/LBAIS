package service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.dao.Curriculum;
import service.dao.Time;
import service.repository.CurriculumRepository;
import service.repository.TimeRepository;
import service.service.CurriculumService;
import service.vo.CurriculumVO;

@Service
public class CurriculumImpl implements CurriculumService {
	@Autowired
	private CurriculumRepository curriculumRepo;
	@Autowired
	private TimeRepository timeRepo;

	@Override
	public List<CurriculumVO> getCurriculum(int cid) {
		List<CurriculumVO> list = new ArrayList<CurriculumVO>();
		List<Time> tList = timeRepo.findByCid(cid);
		List<Curriculum> cList = curriculumRepo.findByCid(cid);

		if (tList == null || tList.isEmpty())
			return list;

		for (Time t : tList) {
			CurriculumVO tc = new CurriculumVO(t.getId(), t.getTime(), 0, "");
			list.add(tc);
			for (int i = 1; i <= 7; i++) {
				CurriculumVO cv = new CurriculumVO(t.getId(), t.getTime(), i, "");
				if (cList != null) {
					for (Curriculum c : cList) {
						if (c.getTid() == t.getId() && c.getDay() == i) {
							cv.setCourse(c.getCourse());
							cList.remove(c);
							break;
						}
					}
				}
				list.add(cv);
			}
		}

		return list;
	}

	@Override
	public int addTime(int id, int cid, String time) {
		if (id == -1) {
			Time t = new Time();
			t.setCid(cid);
			t.setTime(time);
			Time nt = timeRepo.save(t);
			return nt.getId();
		} else {
			timeRepo.updateTime(id, time);
			return id;
		}
	}

	@Override
	public void addCourse(int cid, int tid, int day, String course) {
		Curriculum current = curriculumRepo.findByTidAndCidAndDay(tid, cid, day);
		if (current == null) {
			Curriculum c = new Curriculum();
			c.setCid(cid);
			c.setTid(tid);
			c.setDay(day);
			c.setCourse(course);
			curriculumRepo.save(c);
		} else {
			curriculumRepo.updateCurriculum(course, cid, tid, day);
		}
	}

	@Override
	public void deleteOneLine(int cid, int tid) {
		timeRepo.deleteTime(tid);
		curriculumRepo.deleteCurriculum(cid, tid);

	}
}
