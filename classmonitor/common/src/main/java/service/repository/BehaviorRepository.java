package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import service.dao.Behavior;
import service.vo.LivenessDataVO;


import java.util.List;

/**
 * Created by elva on 2018/4/30.
 */
public interface BehaviorRepository extends JpaRepository<Behavior, Integer> {
    public List<Behavior> findBehaviorsByCidAndSid(int cid,int sid);

    @Query("SELECT b from Behavior b WHERE b.cid=?1 AND b.sid=?2 and (b.behavior='缺勤' OR b.behavior = '迟到'OR b.behavior='早退') AND b.date>?3 ORDER BY date DESC")
    public List<Behavior> findAbsentee(int cid,int sid,String period);

    /**
     //迟到算0.5课时（不管有无请假）
     double lateForClass = 0;
     //早退算0.5课时（不管有无请假）
     double earlyOut = 0;
     //缺勤请假算1课时；自习缺勤请假不算入课时
     double absentee = 0;
     //缺勤旷课算半天的课时（假设每天的课时相等）；自习旷课算1课时
     double cuttingSchool=0;
     */
    @Query("SELECT count(id)\n" +
            "FROM Behavior\n" +
            "WHERE cid = ?1 AND sid = ?2 AND date >= ?3 AND behavior = '迟到'")
    public int countLateForClass(int cid,int sid,String period);

    @Query("SELECT count(id)\n" +
            "FROM Behavior\n" +
            "WHERE cid = ?1 AND sid = ?2 AND date >= ?3 AND behavior = '早退'")
    public int countEarlyOut(int cid,int sid,String period);

    @Query("SELECT count(id)\n" +
            "FROM Behavior\n" +
            "WHERE cid = ?1 AND sid = ?2 AND date >= ?3 AND behavior = '请假'AND status='已请假' AND place <>'自习'")
    public int countAbsentee(int cid,int sid,String period);

    @Query("SELECT count(id)\n" +
            "FROM Behavior\n" +
            "WHERE cid = ?1 AND sid = ?2 AND date >= ?3 AND behavior = '请假'AND status<>'已请假' AND place <>'自习'")
    public int countCuttingLesson(int cid,int sid,String period);

    @Query("SELECT count(id)\n" +
            "FROM Behavior\n" +
            "WHERE cid = ?1 AND sid = ?2 AND date >= ?3 AND behavior = '请假'AND status<>'已请假' AND place ='自习'")
    public int countCuttingStudy(int cid,int sid,String period);

    /**
     *     private String date;
     private String Subject;
     //个人举手次数
     private double timesOfHandsUp;
     //提问次数
     private double timesOfAllHandsUp;
     //发呆时长
     private double timeOfDull;
     //缺勤时长
     private double timeOfAbsentee;
     //课时长
     private double timeOfLesson;
     */

//    @Query("SELECT new service.vo.LivenessDataVO(date,place,count(id))from Behavior where cid=?1 and sid = ?2 and date>=?3 and behavior = '举手' group by date,place")
//    public List<LivenessDataVO> countHandsUp(int cid, int sid, String period);



}
