package service.repository.study;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.entity.study.Pattern;

public interface PatternRepository extends JpaRepository<Pattern, Integer> {
	
	@Query("Select p from Pattern p where p.date>=?1 and p.isHandle!=2 and p.class_id=?2")
	public List<Pattern> getPattern(int date,int cid);
	
	public Pattern findById(int id);
	
	public Pattern save(Pattern pattern);

	
	//修改模式的状态
	@Transactional
	@Modifying
	@Query("update Pattern p set p.isHandle=?2 where p.id=?1")
	public void updateById(int id, int isHandle);

	@Transactional
	@Modifying
	@Query("delete from Pattern p where p.id=?1")
	public void deleteById(int id);

}
