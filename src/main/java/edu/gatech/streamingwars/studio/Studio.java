package edu.gatech.streamingwars.studio;

import edu.gatech.streamingwars.user.AppUser;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class Studio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn
    private AppUser appUser;

    @Column
    private BigDecimal currentRevenue;

    @Column
    private BigDecimal previousRevenue;

    @Column
    private BigDecimal totalRevenue;

}
