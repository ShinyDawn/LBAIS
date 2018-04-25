package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.dao.Test;

public interface TestRepository extends JpaRepository<Test, Integer>{
	
	@Query("select count(t.id) from Test t where t.cid=?1 and t.subjectid=?2 group by t.cid,t.subjectid")
	public Integer getTestNum(int cid,int sid);

	@Transactional
	@Modifying
	@Query("delete from Test t where t.id=?1")
	public void delete(int id);
}
