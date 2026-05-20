package edu.gatech.streamingwars.streamingservice;

import edu.gatech.streamingwars.user.AppUser;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class StreamingService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn
    private AppUser appUser;

    @Column
    private BigDecimal subscriptionPrice;

    @Column
    private BigDecimal currentRevenue;

    @Column
    private BigDecimal previousRevenue;

    @Column
    private BigDecimal totalRevenue;

    @Column
    private BigDecimal licenseFeePaid;
}
