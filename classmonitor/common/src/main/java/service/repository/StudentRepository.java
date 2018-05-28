package service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "truncate table student", nativeQuery = true)
	public void deleteAll();

	public int countStudentsByCid(int cid);

	@Query("select s.name from Student s where s.cid=?1 and s.sid=?2")
	public String getName(int cid,int sid);

	@Query("select s.name from Student s where s.cid=?1 and s.sid=?2")
	public String findName(int cid, int sid);

	public Student findByCidAndSid(int cid,int sid);

	@Query("select s.sid from Student s where s.cid=?1 order by s.sid")
	public List<Integer> getSid(int cid);

	@Query("select s from Student s where s.cid=?1 order by s.sid")
	public List<Student> findByCid(int cid);

	public Student findBySid(int sid);

	@Query("select count(s.sid) from Student s where s.cid=?1 and s.sid=?2")
	public int countSid(int cid, int sid);
	
	@Query("select count(s.sid) from Student s where s.cid=?1 and s.sid<>?2 and s.x=?3 and s.y=?4")
	public int countSeat(int cid, int sid, int x, int y);

	@Transactional
	@Modifying
	@Query("update Student s set s.sid=?2,s.name=?3,s.gender=?4,s.x=?5,s.y=?6 where s.id=?1")
	public void updateOne(int id, int sid, String name, String gender, int x, int y);

	@Transactional
	@Modifying
	@Query("delete from Student s where s.id=?1")
	public void deleteOne(int id);
}

