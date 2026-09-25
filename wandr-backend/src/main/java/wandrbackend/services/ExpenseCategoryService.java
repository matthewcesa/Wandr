package wandrbackend.services;

import wandrbackend.entity.ExpenseCategory;
import java.util.List;

public interface ExpenseCategoryService {
    List<ExpenseCategory> getAllExpenseCategories();
    ExpenseCategory getExpenseCategoryById(Long expenseCategoryId);
    ExpenseCategory createExpenseCategory(ExpenseCategory expenseCategory);
}