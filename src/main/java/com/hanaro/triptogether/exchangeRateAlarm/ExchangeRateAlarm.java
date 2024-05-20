package com.hanaro.triptogether.exchangeRateAlarm;
import com.hanaro.triptogether.exchangeRate.ExchangeRate;
import com.hanaro.triptogether.team.domain.Team;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Exchange_rate_alarm")
public class ExchangeRateAlarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmIdx;

    @ManyToOne
    @JoinColumn(name = "gathering_idx", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "cur_idx", nullable = false)
    private ExchangeRate exchangeRate;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal maxRate;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal minRate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long createdBy;

    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;
}
