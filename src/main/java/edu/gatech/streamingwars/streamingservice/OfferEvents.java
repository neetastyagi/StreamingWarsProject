package edu.gatech.streamingwars.streamingservice;

import edu.gatech.streamingwars.studio.Event;
import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class OfferEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn
    private Event event;

    @Column(nullable = false)
    private BigDecimal viewingPrice;

    @OneToOne
    @JoinColumn
    private StreamingService streamingService;
}
