package edu.gatech.streamingwars.account;

import edu.gatech.streamingwars.admin.DemographicGroup;
import edu.gatech.streamingwars.streamingservice.OfferEvents;
import edu.gatech.streamingwars.streamingservice.StreamingService;

import java.util.List;

public interface AccountService {

    List<StreamingService> getStreamingServices();

    List<OfferEvents> getEventsByName(Long id);

    void subscribeStreamingService(AccountUser accountUser, StreamingService service);

    List<DemographicGroup> getDemographicGroups();

    void joinDemographicGroup(AccountUser accountUser);

    void addWatchEvent(AccountUser accountUser, OfferEvents offerEvents);

    AccountUser findAccountUser(String userName);
}
