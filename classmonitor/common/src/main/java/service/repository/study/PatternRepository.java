package service.repository.study;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.entity.study.Pattern;

public interface PatternRepository extends JpaRepository<Pattern, Integer> {
	
	@Query("Select p from Pattern p where p.date>=?1 and p.isHandle!=2 and p.class_id=?2 order by p.isHandle, p.date desc")
	public List<Pattern> getPattern(int date,int cid);
	
	public Pattern findById(int id);
	
	public Pattern save(Pattern pattern);
	
	@Query("Select count(p) from Pattern p where p.class_id=?1 and p.isHandle=0")
	public int getNum_handle(int cid);

	
	//修改模式的状态
	@Transactional
	@Modifying
	@Query("update Pattern p set p.isHandle=?2 where p.id=?1")
	public void updateIsHandle(int id, int isHandle);
	
	//修改模式的状态
	@Transactional
	@Modifying
	@Query("update Pattern p set p.destribute=?2 where p.id=?1")
	public void updateDestribute(int id, String description);

	@Transactional
	@Modifying
	@Query("delete from Pattern p where p.id=?1")
	public void deleteById(int id);

}
