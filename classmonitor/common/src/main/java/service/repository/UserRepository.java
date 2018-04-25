package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import service.dao.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUidAndPassword(String uid, String password);

	public User findById(int id);

	@Transactional
	@Modifying
	@Query("update User u set u.uid=?2, u.name=?3, u.email=?4, u.tel=?5, u.password=?6 where u.id=?1")
	public void updateById(int id, String uid, String name, String email, String tel, String password);

	@Transactional
	@Modifying
	@Query("delete from User u where u.id=?1")
	public void deleteById(int id);
}
