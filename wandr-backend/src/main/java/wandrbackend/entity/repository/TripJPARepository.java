package wandrbackend.entity.repository;

import org.springframework.data.repository.CrudRepository;
import wandrbackend.entity.User;

public interface TripJPARepository extends CrudRepository<User,Long> {

}
