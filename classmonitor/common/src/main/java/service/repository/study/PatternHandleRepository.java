package service.repository.study;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.entity.study.PatternHandle;

public interface PatternHandleRepository extends JpaRepository<PatternHandle, Integer> {
	@Query("Select h from PatternHandle h where h.pid=?2")
	public List<PatternHandle> getPatternHandle(String pid);
	
	public PatternHandle save(PatternHandle handle);

	@Transactional
	@Modifying
	@Query("delete from PatternHandle h where h.pid=?1")
	public void deleteById(int pid);
}
