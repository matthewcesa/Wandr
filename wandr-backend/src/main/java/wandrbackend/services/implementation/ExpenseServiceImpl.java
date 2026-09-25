package wandrbackend.services.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wandrbackend.entity.*;
import wandrbackend.entity.repository.*;
import wandrbackend.exception.*;
import wandrbackend.services.ExpenseService;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseServiceImpl.class);

    private final ExpenseJPARepository expenseRepository;
    private final TripJPARepository tripRepository;
    private final MemoryJPARepository memoryRepository;
    private final ExpenseCategoryJPARepository expenseCategoryRepository;

    @Autowired
    public ExpenseServiceImpl(ExpenseJPARepository expenseRepository,
                              TripJPARepository tripRepository,
                              MemoryJPARepository memoryRepository,
                              ExpenseCategoryJPARepository expenseCategoryRepository) {
        this.expenseRepository = expenseRepository;
        this.tripRepository = tripRepository;
        this.memoryRepository = memoryRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
    }

    @Override
    public List<Expense> getAllExpensesByTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found: " + tripId));
        logger.info("Get expenses for trip {}", tripId);
        return trip.getExpenses();
    }

    @Override
    public Expense getExpenseById(Long expenseId) {
        logger.info("Get expense {}", expenseId);
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found: " + expenseId));
    }

    @Override
    public Expense createExpense(Long tripId, Long expenseCategoryId, Long memoryId, Expense expense) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found: " + tripId));
        ExpenseCategory category = expenseCategoryRepository.findById(expenseCategoryId)
                .orElseThrow(() -> new ExpenseCategoryNotFoundException("Expense category not foun: " + expenseCategoryId));

        expense.setTrip(trip);
        expense.setExpenseCategory(category);

        if (memoryId != null) {
            Memory memory = memoryRepository.findById(memoryId)
                    .orElseThrow(() -> new MemoryNotFoundException("Memory not found: " + memoryId));
            expense.setMemory(memory);
        }

        logger.info("Create expense for trip {}", tripId);
        return expenseRepository.save(expense);
    }

    @Override
    public Expense updateExpense(Long expenseId, Expense updatedExpense) {
        Expense existing = getExpenseById(expenseId);
        existing.setLabel(updatedExpense.getLabel());
        existing.setAmount(updatedExpense.getAmount());
        existing.setCurrency(updatedExpense.getCurrency());
        existing.setSpentAt(updatedExpense.getSpentAt());
        logger.info("Updating expense {}", expenseId);
        return expenseRepository.save(existing);
    }

    @Override
    public void deleteExpense(Long expenseId) {
        Expense existing = getExpenseById(expenseId);
        logger.info("Delete expense {}", expenseId);
        expenseRepository.delete(existing);
    }
}