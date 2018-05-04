package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import service.entity.Behavior;


import java.util.List;

/**
 * Created by elva on 2018/4/30.
 */
public interface BehaviorRepository extends JpaRepository<Behavior, Integer> {

    @Query("select b from Behavior b where b.cid=?1 and b.sid=?2 and (b.action='缺勤' or b.action = '迟到'or b.action='早退')and b.date>?3 order by b.date desc")
    public List<Behavior> findAbsentee(int cid, int sid, String period);

    /**
     * //迟到算0.5课时（不管有无请假）
     * double lateForClass = 0;
     * //早退算0.5课时（不管有无请假）
     * double earlyOut = 0;
     * //缺勤请假算1课时；自习缺勤请假不算入课时
     * double absentee = 0;
     * //缺勤旷课算半天的课时（假设每天的课时相等）；自习旷课算1课时
     * double cuttingSchool=0;
     */
    @Query("SELECT count(id)\n" +
            "FROM Behavior\n" +
            "WHERE cid = ?1 AND sid = ?2 AND date >= ?3 AND action = '迟到'")
    public int countLateForClass(int cid, int sid, String period);

    @Query("SELECT count(id)\n" +
            "FROM Behavior\n" +
            "WHERE cid = ?1 AND sid = ?2 AND date >= ?3 AND action = '早退'")
    public int countEarlyOut(int cid, int sid, String period);

    @Query("SELECT count(id)\n" +
            "FROM Behavior\n" +
            "WHERE cid = ?1 AND sid = ?2 AND date >= ?3 AND action = '缺勤'AND status='已请假' AND place <>'自习'")
    public int countAbsentee(int cid, int sid, String period);

    @Query("SELECT count(id)\n" +
            "FROM Behavior\n" +
            "WHERE cid = ?1 AND sid = ?2 AND date >= ?3 AND action = '缺勤'AND status<>'已请假' AND place <>'自习'")
    public int countCuttingLesson(int cid, int sid, String period);

    @Query("SELECT count(id)\n" +
            "FROM Behavior\n" +
            "WHERE cid = ?1 AND sid = ?2 AND date >= ?3 AND action = '缺勤'AND status<>'已请假' AND place ='自习'")
    public int countCuttingStudy(int cid, int sid, String period);


    @Query("select coalesce(sum(total_time),0) from Behavior where cid = ?1 and sid = ?2 and date = ?3 and tid = ?4 AND action =?5")
    public int sumTimeOfActionInOneLesson(int cid, int sid, String date, int tid, String action);

    public int countByCidAndSidAndDateAndTidAndAction(int cid, int sid, String date, int tid, String action);

    public int countByCidAndDateAndTidAndAction(int cid, String date, int tid, String action);

    @Query("SELECT count(id) FROM Behavior WHERE cid=?1 AND sid=?2 AND date =?3 AND tid = ?4 AND status = '已请假'")
    public int countApproval(int cid, int sid, String date, int tid);

//    @Query("SELECT new service.vo.LivenessDataVO(date,place,count(id))from Behavior where cid=?1 and sid = ?2 and date>=?3 and behavior = '举手' group by date,place order by date,place asc ")
//    public List<LivenessDataVO> countHandsUp(int cid, int sid, String period);
//
//    @Query("SELECT new service.vo.LivenessDataVO(date,place,count(id))from Behavior where cid=?1 and sid = ?2 and date>=?3 and behavior = '举手' group by date,place order by date,place asc ")
//    public List<LivenessDataVO> countHandsUpByPlace(int cid, int sid,String place, String period);


}
