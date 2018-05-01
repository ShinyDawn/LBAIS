package service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.dao.Curriculum;

public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {

	public List<Curriculum> findByCid(int cid);
	
	public Curriculum findByTidAndCidAndDay(int tid,int cid,int day);

	@Query("SELECT count(id)\n" +
			"FROM Curriculum \n" +
			"WHERE cid = ?1 AND day = 1")
	public int findHowManyLessonsOneDay(int cid);
	
	@Transactional
	@Modifying
	@Query("update Curriculum c set c.course=?1 where c.cid=?2 and c.tid=?3 and c.day=?4")
	public void updateCurriculum(String course,int cid,int tid, int day);
	
	@Transactional
	@Modifying
	@Query("delete from Curriculum c where c.cid=?1 and c.tid=?2")
	public void deleteCurriculum(int cid,int tid);
}
