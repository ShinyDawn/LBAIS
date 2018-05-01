package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import service.dao.Behavior;

import java.util.List;

/**
 * Created by elva on 2018/4/30.
 */
public interface BehaviorRepository extends JpaRepository<Behavior, Integer> {
    public List<Behavior> findBehaviorsByCidAndSid(int cid,int sid);

    @Query("SELECT b from Behavior b WHERE b.cid=?1 AND b.sid=?2 and (b.behavior='缺勤' OR b.behavior = '迟到'OR b.behavior='早退') AND b.date>?3 ORDER BY date DESC")
    public List<Behavior> findAbsentee(int cid,int sid,String period);

}
