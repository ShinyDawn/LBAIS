package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import service.dao.Absentee;

import java.util.List;

/**
 * Created by elva on 2018/4/30.
 *     private int id;
 //class id
 private int cid;
 //student id in class
 private int sid;
 //date absent; Format:YYYY-MM-DD
 private String date;
 //time period absent
 private int tid;
 //illness or personal affairs
 private String reason;
 */
public interface AbsenteeRepository extends JpaRepository<Absentee, Integer> {

    public List<Absentee> findByCidAndSid(int cid, int sid);

    public List<Absentee> findAbsenteesByCidAndSidAndDateIsAfter(int cid,int sid,String date);

    @Transactional
    @Modifying
    @Query("update Absentee a set a.cid=?2,a.sid=?3,a.date=?4,a.tid=?5,a.reason=?6 where a.id=?1")
    public void updateById(int id, int cid,int sid, String date,int tid,String reason);

    @Transactional
    @Modifying
    @Query("delete from Absentee a where a.id=?1")
    public void deleteById(int id);

}
