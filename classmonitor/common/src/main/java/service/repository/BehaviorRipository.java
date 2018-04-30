package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.dao.Behavior;

import java.util.List;

/**
 * Created by elva on 2018/4/30.
 */
public interface BehaviorRipository extends JpaRepository<Behavior, Integer> {
    public List<Behavior> findBehaviorsByCidAndSid(int cid,int sid);
}
