package edu.gatech.streamingwars.account;

import edu.gatech.streamingwars.admin.DemographicGroup;
import edu.gatech.streamingwars.streamingservice.OfferEvents;
import edu.gatech.streamingwars.streamingservice.StreamingService;
import edu.gatech.streamingwars.user.AppUser;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Data
public class AccountUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private BigDecimal currentSpending;

    @Column
    private BigDecimal previousSpending;

    @Column
    private BigDecimal totalSpending;

    @OneToOne
    @JoinColumn
    private AppUser user;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<OfferEvents> watchEvents;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<StreamingService> streamingServices;

    @OneToOne
    @JoinColumn
    private DemographicGroup demographicGroup;

}
