package edu.gatech.streamingwars.streamingservice;

import edu.gatech.streamingwars.account.AccountRepository;
import edu.gatech.streamingwars.account.AccountUser;
import edu.gatech.streamingwars.studio.EventRepository;
import edu.gatech.streamingwars.studio.Studio;
import edu.gatech.streamingwars.studio.StudioRepository;
import edu.gatech.streamingwars.user.AppUser;
import edu.gatech.streamingwars.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StreamingServiceServiceImpl implements StreamingServiceService {

    private final OfferEventsRepository offerEventsRepository;
    private final StreamingServiceRepository streamingServiceRepository;
    private final UserRepository userRepository;
    private final StudioRepository studioRepository;
    private final EventRepository eventRepository;
    private final AccountRepository accountRepository;

    @Override
    public void addOfferEvent(OfferEvents offerEvent) {
        offerEventsRepository.save(offerEvent);
    }

    @Override
    public void updateOfferedEventViewingPrice(Long id, BigDecimal newPrice) {
        if(newPrice != null) {
            Optional<OfferEvents> newEvent = offerEventsRepository.findById(id);
            OfferEvents updateOfferedEvent = newEvent.get();
            updateOfferedEvent.setViewingPrice(newPrice);
            offerEventsRepository.save(updateOfferedEvent);
        }
    }

    @Override
    public List<OfferEvents> getOfferEventsList(String userName) {
        StreamingService service = findStreamingServiceUser(userName);
        List<OfferEvents> offerEventsList = offerEventsRepository.findByStreamingService_AppUser_FirstName(service.getAppUser().getFirstName());
        return offerEventsList;
    }

    @Override
    public List<Studio> getStudios() {
        List<Studio> studioList = studioRepository.findAll();
        return studioList;
    }

    @Override
    public StreamingService findStreamingServiceUser(String name) {
        AppUser appuser = userRepository.findByUserName(name);
        StreamingService streamingService = streamingServiceRepository.findStreamingServiceByAppUser(appuser);
        return streamingService;
    }

    @Override
    public void updateStreamingService(StreamingService streamingService, String userName) {
        AppUser appuser = streamingService.getAppUser();
        StreamingService myStreamingService = findStreamingServiceUser(userName);
        AppUser myAppuser = myStreamingService.getAppUser();
        myAppuser.setLastName(appuser.getLastName());
        myStreamingService.setAppUser(myAppuser);
        streamingServiceRepository.save(myStreamingService);
    }

    @Override
    public void updateSubscriptionPrice(StreamingService streamingService, String userName) {
        StreamingService myStreamingService = findStreamingServiceUser(userName);
        myStreamingService.setSubscriptionPrice(streamingService.getSubscriptionPrice());
        streamingServiceRepository.save(myStreamingService);
    }

    @Override
    public void payLicenseFeeToStudio(String userName, Studio studio, BigDecimal fee) {
        BigDecimal currentRevenue = BigDecimal.valueOf(0.0);
        if (studio.getCurrentRevenue() != null) {
            currentRevenue = studio.getCurrentRevenue();
        }
        studio.setCurrentRevenue(addBigDecimals(currentRevenue, fee));
        studioRepository.save(studio);

        StreamingService myStreamingService = findStreamingServiceUser(userName);

        BigDecimal licenseFeePaid = BigDecimal.valueOf(0.0);
        if(myStreamingService.getLicenseFeePaid() != null) {
            licenseFeePaid = myStreamingService.getLicenseFeePaid();
        }

        myStreamingService.setLicenseFeePaid(addBigDecimals(licenseFeePaid, fee));
        streamingServiceRepository.save(myStreamingService);
    }

    @Override
    public void deleteOfferedEvent(Long id) {
        Optional<OfferEvents> newEvent = offerEventsRepository.findById(id);
        OfferEvents offerEvents = newEvent.get();
        List<AccountUser> accountUsers = accountRepository.findAll();
        Boolean deleteEvent = true;

        for(AccountUser user : accountUsers) {
            Collection<OfferEvents> watchEvents = user.getWatchEvents();
            for(OfferEvents event : watchEvents) {
                if(event.getEvent().getEventId() == offerEvents.getEvent().getEventId()) {
                    deleteEvent = false;
                    break;
                }
            }
        }

        if(deleteEvent) {
            offerEventsRepository.delete(offerEvents);
        }
    }

    private BigDecimal addBigDecimals(BigDecimal a, BigDecimal b) {
        BigDecimal sum;
        sum = a.add(b);
        return sum;
    }
}
