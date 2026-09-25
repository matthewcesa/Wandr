package wandrbackend.entity.repository;

import org.springframework.data.repository.CrudRepository;
import wandrbackend.entity.Memory;

public interface MemoryJPARepository extends CrudRepository<Memory,Long> {

}
