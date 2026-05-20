package edu.gatech.streamingwars.streamingservice;

import edu.gatech.streamingwars.studio.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OfferEventsRepository extends JpaRepository<OfferEvents, Long> {
    List<OfferEvents> findByStreamingService_AppUser_FirstName(String serviceName);

    List<OfferEvents> findByStreamingService_AppUser_Id(Long id);

    List<OfferEvents> findOfferEventsByEvent(Event event);
}
