package service.service;

import java.util.List;

import service.vo.SubjectVO;

public interface SubjectService {

	public List<SubjectVO> getSubject(int cid);
	
	public int upload(String fileName, int cid, int sid, String date);

	public int add(int cid, String name, String tname);

	public int update(int id, int cid, String name, String tname);

	public void delete(int id);
}
