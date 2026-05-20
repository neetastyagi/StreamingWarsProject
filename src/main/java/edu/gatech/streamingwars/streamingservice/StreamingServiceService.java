package edu.gatech.streamingwars.streamingservice;

import edu.gatech.streamingwars.studio.Studio;
import java.math.BigDecimal;
import java.util.List;

public interface StreamingServiceService {

    void addOfferEvent(OfferEvents offerEvent);

    void updateOfferedEventViewingPrice(Long id, BigDecimal newPrice);

    List<OfferEvents> getOfferEventsList(String userName);

    List<Studio> getStudios();

    StreamingService findStreamingServiceUser(String name);

    void updateStreamingService(StreamingService streamingService, String userName);

    void updateSubscriptionPrice(StreamingService streamingService, String userName);

    void payLicenseFeeToStudio(String userName, Studio studio, BigDecimal fee);

    void deleteOfferedEvent(Long id);
}
