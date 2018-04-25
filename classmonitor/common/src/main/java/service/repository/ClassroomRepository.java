package service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.dao.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

	@Query("Select c from Classroom c order by c.grade, c.date")
	public List<Classroom> findAllClasses();
	
	public Classroom findById(int id);

	@Transactional
	@Modifying
	@Query("update Classroom c set c.cname=?2,c.teacher=?3,c.num=?4,c.grade=?5,c.date=?6 where c.id=?1")
	public void updateById(int id, String cname,String teacher,int num,String grade,String date);

	@Transactional
	@Modifying
	@Query("delete from Classroom c where c.id=?1")
	public void deleteById(int id);
}
