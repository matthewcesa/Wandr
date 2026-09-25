package wandrbackend.entity.repository;

import org.springframework.data.repository.CrudRepository;
import wandrbackend.entity.User;

public interface MemoryJPARepository extends CrudRepository<User,Long> {

}
