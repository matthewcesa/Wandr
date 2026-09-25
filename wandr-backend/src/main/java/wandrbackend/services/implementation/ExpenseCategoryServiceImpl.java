package wandrbackend.services.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wandrbackend.entity.ExpenseCategory;
import wandrbackend.entity.repository.ExpenseCategoryJPARepository;
import wandrbackend.exception.ExpenseCategoryNotFoundException;
import wandrbackend.services.ExpenseCategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseCategoryServiceImpl.class);

    private final ExpenseCategoryJPARepository expenseCategoryRepository;

    @Autowired
    public ExpenseCategoryServiceImpl(ExpenseCategoryJPARepository expenseCategoryRepository) {
        this.expenseCategoryRepository = expenseCategoryRepository;
    }

    @Override
    public List<ExpenseCategory> getAllExpenseCategories() {
        logger.info("get all expense categories");
        List<ExpenseCategory> categories = new ArrayList<>();
        expenseCategoryRepository.findAll().forEach(categories::add);
        return categories;
    }

    @Override
    public ExpenseCategory getExpenseCategoryById(Long expenseCategoryId) {
        logger.info("Get expense category {}", expenseCategoryId);
        return expenseCategoryRepository.findById(expenseCategoryId)
                .orElseThrow(() -> new ExpenseCategoryNotFoundException("Expense category not found: " + expenseCategoryId));
    }

    @Override
    public ExpenseCategory createExpenseCategory(ExpenseCategory expenseCategory) {
        logger.info("Create expense category '{}'", expenseCategory.getName());
        return expenseCategoryRepository.save(expenseCategory);
    }
}