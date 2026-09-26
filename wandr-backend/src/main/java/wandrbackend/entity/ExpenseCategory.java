package wandrbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "expenses_categories")
public class ExpenseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseCategoryId;

    @Column(nullable = false, unique = true)
    private String name;

    private String icon;

    @OneToMany(mappedBy = "expenseCategory")
    @JsonIgnore
    private List<Expense> expenses;


    public Long getExpenseCategoryId() {
        return expenseCategoryId;
    }

    public void setExpenseCategoryId(Long expenseCategoryId) {
        this.expenseCategoryId = expenseCategoryId;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}