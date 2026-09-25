package wandrbackend.entity.repository;

import org.springframework.data.repository.CrudRepository;
import wandrbackend.entity.ExpenseCategory;

public interface ExpenseCategoryJPARepository extends CrudRepository<ExpenseCategory, Long> {
}
