package service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

	public List<Subject> findByCid(int cid);
	
	public Subject findByCidAndName(int cid, String name);

	@Query("select s from Subject s where s.id<>?1 and s.cid=?2 and s.name=?3")
	public Subject findByIdAndCidAndName(int id, int cid, String name);

	@Transactional
	@Modifying
	@Query("update Subject s set s.cid=?2,s.name=?3,s.tname=?4 where s.id=?1")
	public int update(int id, int cid, String name, String tname);

	@Transactional
	@Modifying
	@Query("delete from Subject s where s.id=?1")
	public void delete(int id);
}
