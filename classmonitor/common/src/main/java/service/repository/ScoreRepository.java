package service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.dao.Score;
import service.vo.ScoreVO;

public interface ScoreRepository extends JpaRepository<Score, Integer> {
	
	@Query("select avg(s.score) from Score s where s.cid=?1 and s.subjectid=?2 group by s.cid,s.subjectid")
	public Double average(int cid,int sid);
	
	@Query("select new service.vo.ScoreVO(s.id,avg(sc.score),t.date) from Subject s,Test t,Score sc where s.cid=?1 and t.cid=s.cid and sc.cid=s.cid group by sc.testid order by t.date")
	public List<ScoreVO> findByCidAndSid(int cid,int sid);
	
	@Query("select s from Score s where s.cid=?1 and s.testid=(select t.id from Test t where t.cid=?1 and t.date=(select max(t2.date) from Test t2 where t2.cid=?1 group by t2.cid)) order by s.score")
	public List<Score> findByCid(int cid);

	@Transactional
	@Modifying
	@Query("delete from Score s where s.testid=?2")
	public void delete(int tid);
}
