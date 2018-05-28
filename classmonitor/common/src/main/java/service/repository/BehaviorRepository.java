package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.entity.Behavior;

import java.util.List;

/**
 * Created by elva on 2018/4/30.
 */
public interface BehaviorRepository extends JpaRepository<Behavior, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "truncate table behavior", nativeQuery = true)
	public void deleteAll();
	
	@Query("select b from Behavior b where b.cid=?1 and b.place<>'自习' and (b.action='举手' or b.action='回答问题') and b.date>=?2 order by b.date")
	public List<Behavior> findPositivity(int cid, String date);
	
	@Query("select count(id) from Behavior b where b.cid=?1 and b.place=?2 and (b.action='举手' or b.action='回答问题') and b.date=?3")
	public int findPositivity(int cid, String cname,String date);

	@Query("select count(id) from Behavior b where b.cid=?1 and b.sid=?2 and b.action='举手' and b.place=?3 and b.date=?4")
	public int countRaiseHand(int cid, int sid, String cname, String date);

	@Query("select count(id) from Behavior b where b.cid=?1 and b.sid=?2 and b.action='回答问题' and b.place=?3 and b.date=?4")
	public int countAnswerQuestion(int cid, int sid, String cname, String date);

	@Transactional
    @Modifying
    @Query("update Behavior a set a.status=?5 where a.cid=?1 and a.sid=?2 and a.date=?3 and a.tid=?4 and(a.action = '缺勤' or a.action='迟到'or a.action='早退' or a.action='离开') ")
    public int updateStatus(int cid, int sid, String date, int tid, String status);

    @Transactional
    @Modifying
    @Query("update Behavior a set a.end=?6,a.action=?7,a.total_time=?1 where a.cid=?2 and a.sid=?3 and a.date=?4 and a.start=?5 and a.action=?8")
    public int updateByTime(int totalTime, int cid, int sid, String date, String start, String end, String action, String actionBefore);


    @Query("select b from Behavior b where b.cid=?1 and b.sid=?2 and (b.action='缺勤' or b.action = '迟到'or b.action='早退')and b.date>?3 and b.date<=?4 order by b.date desc")
    public List<Behavior> getAbsentee(int cid, int sid, String startDate, String endDate);

    @Query("SELECT count(id) FROM Behavior WHERE cid = ?1 AND sid = ?2 AND date > ?3 and date<=?4 and action=?5")
    public int countDuringPeriod(int cid, int sid, String startDate, String endDate, String action);

    @Query("SELECT b FROM Behavior b WHERE b.cid = ?1 AND b.sid = ?2 AND b.date > ?3 and b.date<=?4 AND b.action = ?5")
    public List<Behavior> findAction(int cid, int sid, String startDate, String endDate, String action);


    @Query("select coalesce(sum(total_time),0) from Behavior where cid = ?1 and sid = ?2 and date = ?3 and tid = ?4 AND action =?5")
    public int sumTimeOfActionInOneLesson(int cid, int sid, String date, int tid, String action);

    public int countByCidAndSidAndDateAndTidAndAction(int cid, int sid, String date, int tid, String action);

    public int countByCidAndDateAndTidAndAction(int cid, String date, int tid, String action);

    //    @Query("SELECT count(id) FROM Behavior WHERE cid=?1 AND sid=?2 AND date =?3 AND tid = ?4 AND status =?5")
    public int countByCidAndSidAndDateAndTidAndStatus(int cid, int sid, String date, int tid, String status);

    @Query("SELECT b FROM Behavior b WHERE b.cid = ?1 AND b.sid = ?2 AND b.date > ?3 and b.date<=?4 AND b.place = '自习' AND b.status <> '误报'")
    public List<Behavior> findBehaviorsDuringStudy(int cid, int sid, String startDate, String endDate);
}






