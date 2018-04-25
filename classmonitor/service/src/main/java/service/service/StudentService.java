package service.service;

import java.util.List;

import service.dao.Student;

public interface StudentService {

	public List<Student> getInfo(int cid);

	public int add(int cid, int sid, String name, String gender, int x, int y);

	public int update(int id, int cid, int sid, String name, String gender, int x, int y);

	public void delete(int id);
}
