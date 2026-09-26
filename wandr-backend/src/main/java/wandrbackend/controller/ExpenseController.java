package wandrbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wandrbackend.entity.Expense;
import wandrbackend.services.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/trips/{tripId}/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Expense> getAllExpensesByTrip(@PathVariable("tripId") Long tripId) {
        return expenseService.getAllExpensesByTrip(tripId);
    }

    @GetMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.OK)
    public Expense getExpenseById(@PathVariable("expenseId") Long expenseId) {
        return expenseService.getExpenseById(expenseId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Expense createExpense(
            @PathVariable("tripId") Long tripId,
            @RequestParam Long expenseCategoryId,
            @RequestParam(required = false) Long memoryId,
            @RequestBody Expense expense) {
        return expenseService.createExpense(tripId, expenseCategoryId, memoryId, expense);
    }

    @PutMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.OK)
    public Expense updateExpense(@PathVariable("expenseId") Long expenseId, @RequestBody Expense expense) {
        return expenseService.updateExpense(expenseId, expense);
    }

    @DeleteMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable("expenseId") Long expenseId) {
        expenseService.deleteExpense(expenseId);
    }
}
