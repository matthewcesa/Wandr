package wandrbackend.services;

import wandrbackend.entity.Expense;
import java.util.List;

public interface ExpenseService {
    List<Expense> getAllExpensesByTrip(Long tripId);
    Expense getExpenseById(Long expenseId);
    Expense createExpense(Long tripId, Long expenseCategoryId, Long memoryId, Expense expense);
    Expense updateExpense(Long expenseId, Expense updatedExpense);
    void deleteExpense(Long expenseId);
}