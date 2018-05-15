package service.repository.study;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.entity.study.AlarmHandle;

public interface AlarmHandleRepository extends JpaRepository<AlarmHandle, Integer> {
	public List<AlarmHandle> findByAid(String aid);
	
	public AlarmHandle save(AlarmHandle handle);

	@Transactional
	@Modifying
	@Query("delete from AlarmHandle a where a.aid=?1")
	public void deleteById(int aid);
}
