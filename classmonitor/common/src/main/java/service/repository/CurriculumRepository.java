package service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.entity.Curriculum;
import service.vo.LessonDataVO;

public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {

    public List<Curriculum> findByCid(int cid);

    public Curriculum findByTidAndCidAndDay(int tid, int cid, int day);


    @Query("SELECT count(id) FROM Curriculum where cid = ?1 AND date>?2 and date<=?3 and course=?4 ")
    public int countWeight(int cid, String start, String end, String subject);

    @Query("SELECT count(id)\n" +
            "FROM Curriculum \n" +
            "WHERE cid = ?1 AND date='2018-04-25'")
    public int countCoursesOneDay(int cid);

    @Query("SELECT new service.vo.LessonDataVO(date,tid) FROM Curriculum WHERE cid = ?1 AND course=?2 AND  date >?3 and date<=?4 ORDER BY date,tid ASC")
    public List<LessonDataVO> getDistinctCourse(int cid, String course, String strat, String end);

    @Query("SELECT DISTINCT course from Curriculum WHERE cid = ?1")
    public List<String> getDistinctSubject(int cid);

    @Query("SELECT count(id) from Curriculum where cid=?1 and date>?2 AND date<=?3")
    public int countTotalCourses(int cid, String start, String end);

    @Query("SELECT count(id) from Curriculum where cid=?1 and date>?2 AND date<=?3 AND course='自习'")
    public int countTotalStudys(int cid, String start, String end);

    @Transactional
    @Modifying
    @Query("update Curriculum c set c.course=?1 where c.cid=?2 and c.tid=?3 and c.day=?4")
    public void updateCurriculum(String course, int cid, int tid, int day);

    @Transactional
    @Modifying
    @Query("delete from Curriculum c where c.cid=?1 and c.tid=?2")
    public void deleteCurriculum(int cid, int tid);
}
