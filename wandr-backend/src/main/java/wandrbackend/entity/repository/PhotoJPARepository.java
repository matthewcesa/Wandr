package wandrbackend.entity.repository;

import org.springframework.data.repository.CrudRepository;
import wandrbackend.entity.Photo;

public interface PhotoJPARepository extends CrudRepository<Photo,Long> {

}
