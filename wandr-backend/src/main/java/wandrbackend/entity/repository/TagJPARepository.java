package wandrbackend.entity.repository;

import org.springframework.data.repository.CrudRepository;
import wandrbackend.entity.Tag;

public interface TagJPARepository extends CrudRepository<Tag,Long> {

}
