package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import service.entity.CourseCal;

import java.util.List;

/**
 * Created by elva on 2018/5/28.
 */
public interface CourseCalRepository extends JpaRepository<CourseCal, Integer> {

    @Transactional
    @Modifying
    @Query(value = "truncate table course_cal", nativeQuery = true)
    public void deleteAll();

    @Query("select c from CourseCal c where c.cid=?1 and c.sid =?2 and c.subject=?3 and c.date>?4 and c.date<=?5")
    public List<CourseCal> findAll(int cid,int sid,String subject,String start,String end);
}
