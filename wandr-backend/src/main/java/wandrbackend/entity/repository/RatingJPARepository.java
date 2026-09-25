package wandrbackend.entity.repository;

import org.springframework.data.repository.CrudRepository;
import wandrbackend.entity.Rating;

public interface RatingJPARepository extends CrudRepository<Rating,Long> {

}
