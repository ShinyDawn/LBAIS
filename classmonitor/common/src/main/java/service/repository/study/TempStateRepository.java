package service.repository.study;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import service.entity.Classroom;
import service.entity.study.TempState;

public interface TempStateRepository extends JpaRepository<TempState, Integer>{
	
	public List<TempState> findByNumAndCidAndSequence(int num,int cid,int sequence);
	
	public TempState findById(int id);
	
	@Query("Select max(sequence) from TempState t where num=?1 and cid=?2")
	public int getMaxSeq(int num,int cid);
}
