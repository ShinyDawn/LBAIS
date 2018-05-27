package service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.entity.Time;

import javax.rmi.CORBA.Tie;

public interface TimeRepository extends JpaRepository<Time, Integer>{

//	@Query("SELECT t from Time t where t.cid =?1  and t.id =?2")
	public Time findById(int id);

	public int countByCid(int cid);
	
	@Query("select t from Time t where t.cid=?1 order by t.time")
	public List<Time> findByCid(int cid);
	
	@Transactional
	@Modifying
	@Query("update Time t set t.time=?2 where t.id=?1")
	public void updateTime(int id,String time);

	@Transactional
	@Modifying
	@Query("delete from Time t where t.id=?1")
	public void deleteTime(int tid);
}
