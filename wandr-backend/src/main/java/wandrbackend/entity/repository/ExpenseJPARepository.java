package wandrbackend.entity.repository;

import org.springframework.data.repository.CrudRepository;
import wandrbackend.entity.Expense;


public interface ExpenseJPARepository extends CrudRepository<Expense,Long> {

}
