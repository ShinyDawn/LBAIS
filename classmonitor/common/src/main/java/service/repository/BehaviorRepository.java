package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import service.entity.Behavior;


import java.util.List;

/**
 * Created by elva on 2018/4/30.
 */
public interface BehaviorRepository extends JpaRepository<Behavior, Integer> {
    @Query("select count(id) from Behavior b where b.cid=?1 and b.sid=?2 and b.action='举手' and b.place=?3 and b.date=?4")
    public int countRaiseHand(int cid, int sid, String cname, String date);

    @Query("select count(id) from Behavior b where b.cid=?1 and b.sid=?2 and b.action='回答问题' and b.place=?3 and b.date=?4")
    public int countAnswerQuestion(int cid, int sid, String cname, String date);

    @Query("select b from Behavior b where b.cid=?1 and b.sid=?2 and (b.action='缺勤' or b.action = '迟到'or b.action='早退')and b.date>?3 and b.date<=?4 order by b.date desc")
    public List<Behavior> findAbsentee(int cid, int sid, String startDate, String endDate);


    @Query("SELECT b FROM Behavior b WHERE b.cid = ?1 AND b.sid = ?2 AND b.date > ?3 and b.date<=?4 AND (b.action = '缺勤' or b.action='迟到'or b.action='早退' )AND b.status<>'已请假'")
    public List<Behavior> findAbsenteeVoilation(int cid, int sid, String startDate, String endDate);

    @Query("SELECT count(id) FROM Behavior WHERE cid = ?1 AND sid = ?2 AND date > ?3 and date<=?4 and action=?5")
    public int countPeriodBehaviorByAction(int cid, int sid, String startDate, String endDate, String action);

    @Query("SELECT b FROM Behavior b WHERE b.cid = ?1 AND b.sid = ?2 AND b.date > ?3 and b.date<=?4 AND b.action = '缺勤'")
    public List<Behavior> findNoShow(int cid, int sid, String startDate, String endDate);


    @Query("select coalesce(sum(total_time),0) from Behavior where cid = ?1 and sid = ?2 and date = ?3 and tid = ?4 AND action =?5")
    public int sumTimeOfActionInOneLesson(int cid, int sid, String date, int tid, String action);

    public int countByCidAndSidAndDateAndTidAndAction(int cid, int sid, String date, int tid, String action);

    public int countByCidAndDateAndTidAndAction(int cid, String date, int tid, String action);

    @Query("SELECT count(id) FROM Behavior WHERE cid=?1 AND sid=?2 AND date =?3 AND tid = ?4 AND status = '已请假'")
    public int countApproval(int cid, int sid, String date, int tid);

    @Query("SELECT b FROM Behavior b WHERE b.cid = ?1 AND b.sid = ?2 AND b.date > ?3 and b.date<=?4 AND b.place = '自习' AND b.status <> '误报'")
    public List<Behavior> findBehaviorsDuringStudy(int cid, int sid, String startDate, String endDate);
}

