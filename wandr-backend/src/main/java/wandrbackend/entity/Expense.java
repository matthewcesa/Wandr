package wandrbackend.entity;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "memory_id")
    private Memory memory;

    @ManyToOne
    @JoinColumn(name = "expense_category_id", nullable = false)
    private ExpenseCategory expenseCategory;

    private String label;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(columnDefinition = "VARCHAR(3) DEFAULT 'EUR'")
    private String currency;

    private LocalDateTime spentAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}