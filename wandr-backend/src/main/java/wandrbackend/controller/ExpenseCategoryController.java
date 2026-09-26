package wandrbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wandrbackend.entity.ExpenseCategory;
import wandrbackend.services.ExpenseCategoryService;

import java.util.List;

@RestController
@RequestMapping("/expense-categories")
public class ExpenseCategoryController {
    private final ExpenseCategoryService expenseCategoryService;

    public ExpenseCategoryController(ExpenseCategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExpenseCategory> getAllExpenseCategories() {
        return expenseCategoryService.getAllExpenseCategories();
    }

    @GetMapping("/{expenseCategoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ExpenseCategory getExpenseCategoryById(@PathVariable("expenseCategoryId") Long expenseCategoryId) {
        return expenseCategoryService.getExpenseCategoryById(expenseCategoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseCategory createExpenseCategory(@RequestBody ExpenseCategory expenseCategory) {
        return expenseCategoryService.createExpenseCategory(expenseCategory);
    }
}
