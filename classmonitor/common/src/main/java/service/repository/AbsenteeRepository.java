package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import service.dao.Absentee;

import java.util.List;

/**
 * Created by elva on 2018/4/30.
 */
public interface AbsenteeRepository extends JpaRepository<Absentee, Integer> {

    @Transactional
    @Modifying
    @Query("update Absentee a set a.cid=?2,a.sid=?3,a.date=?4,a.tid=?5,a.reason=?6 where a.id=?1")
    public void updateById(int id, int cid,int sid, String date,int tid,String reason);

    @Transactional
    @Modifying
    @Query("delete from Absentee a where a.id=?1")
    public void deleteById(int id);

}
