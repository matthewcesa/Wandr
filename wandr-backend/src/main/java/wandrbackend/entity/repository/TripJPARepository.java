package wandrbackend.entity.repository;

import org.springframework.data.repository.CrudRepository;
import wandrbackend.entity.Trip;

public interface TripJPARepository extends CrudRepository<Trip,Long> {

}
