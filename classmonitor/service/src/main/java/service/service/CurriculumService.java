package service.service;

import java.util.List;

import service.vo.CurriculumVO;

public interface CurriculumService {

	public List<CurriculumVO> getCurriculum(int cid);

	public int addTime(int id, int cid, String time);

	public void addCourse(int cid, int tid, int day, String course);

	public void addCourseNew(int cid, int tid, int day, String course,String date);

	public void deleteOneLine(int cid, int tid);
}
