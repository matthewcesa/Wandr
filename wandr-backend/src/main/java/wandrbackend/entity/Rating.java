package wandrbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "memory_id", nullable = false)
    @JsonIgnore
    private Memory memory;

    private BigDecimal valueForMoney;
    private BigDecimal atmosphereAndService;
    private BigDecimal qualityOfActivity;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public BigDecimal getValueForMoney() {
        return valueForMoney;
    }

    public void setValueForMoney(BigDecimal valueForMoney) {
        this.valueForMoney = valueForMoney;
    }

    public BigDecimal getAtmosphereAndService() {
        return atmosphereAndService;
    }

    public void setAtmosphereAndService(BigDecimal atmosphereAndService) {
        this.atmosphereAndService = atmosphereAndService;
    }

    public BigDecimal getQualityOfActivity() {
        return qualityOfActivity;
    }

    public void setQualityOfActivity(BigDecimal qualityOfActivity) {
        this.qualityOfActivity = qualityOfActivity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}