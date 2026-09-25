package wandrbackend.entity;


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
    private List<Expense> expenses;
}