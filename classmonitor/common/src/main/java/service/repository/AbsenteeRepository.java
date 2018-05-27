package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import service.entity.Absentee;

import java.util.List;

/**
 * Created by elva on 2018/4/30.
 */
public interface AbsenteeRepository extends JpaRepository<Absentee, Integer> {

    @Query("SELECT a FROM Absentee a WHERE a.cid=?1 AND a.date>=?2 AND a.date <?3 ORDER BY a.date DESC,a.tid ASC")
    public List<Absentee> findAll(int cid, String start, String end);

//    @Query("SELECT count(a.id) FROM Absentee a WHERE a.cid=?1 AND a.sid=?2 AND a.date=?3 and a.tid=?4")
    public int countByCidAndSidAndDateAndTid(int cid, int sid, String date, int tid);

    @Transactional
    @Modifying
    @Query("update Absentee a set a.cid=?2,a.sid=?3,a.date=?4,a.tid=?5,a.reason=?6,a.type=?7 where a.id=?1")
    public void updateById(int id, int cid, int sid, String date, int tid, String reason, String type);

    @Transactional
    @Modifying
    @Query("delete from Absentee a where a.id=?1")
    public void deleteById(int id);



}
