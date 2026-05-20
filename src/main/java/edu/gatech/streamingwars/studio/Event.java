package edu.gatech.streamingwars.studio;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"eventName", "yearProduced"})
})
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventId;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private int yearProduced;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private BigDecimal licenseFee;

    @OneToOne
    @JoinColumn
    private Studio studio;

}
