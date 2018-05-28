package service.repository.study;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import service.entity.study.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
	@Query("Select a from Alarm a where a.date>=?1 and a.isHandle!=2 and a.class_id=?2 order by a.isHandle, a.date desc")
	public List<Alarm> getAlarm(int date,int cid);
	
	public Alarm findById(int id);
	
	public Alarm save(Alarm alarm);
	
	@Query("Select count(a) from Alarm a where a.class_id=?1 and a.isHandle=1")
	public int getNum_handle(int cid);
	
	@Query("Select count(a) from Alarm a where a.class_id=?1 and a.isHandle=0")
	public int getNum_confirm(int cid);

	
	//修改报警的状态
	@Transactional
	@Modifying
	@Query("update Alarm a set a.isHandle=?2 where a.id=?1")
	public void updateIsHandle(int id, int isHandle);
	
	//修改报警的描述
	@Transactional
	@Modifying
	@Query("update Alarm a set a.destribute=?2 where a.id=?1")
	public void updateDestribute(int id,String description);

	@Transactional
	@Modifying
	@Query("delete from Alarm a where a.id=?1")
	public void deleteById(int id);
}
