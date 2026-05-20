package edu.gatech.streamingwars.account;

import edu.gatech.streamingwars.admin.DemographicGroup;
import edu.gatech.streamingwars.admin.DemographicGroupRepository;
import edu.gatech.streamingwars.streamingservice.OfferEvents;
import edu.gatech.streamingwars.streamingservice.OfferEventsRepository;
import edu.gatech.streamingwars.streamingservice.StreamingService;
import edu.gatech.streamingwars.streamingservice.StreamingServiceRepository;
import edu.gatech.streamingwars.user.AppUser;
import edu.gatech.streamingwars.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final StreamingServiceRepository streamingServiceRepository;
    private final OfferEventsRepository offerEventsRepository;
    private final DemographicGroupRepository demographicGroupRepository;
    private final UserRepository userRepository;

    @Override
    public List<StreamingService> getStreamingServices() {
        List<StreamingService> streamingServiceList = streamingServiceRepository.findAll();
        return streamingServiceList;
    }

    @Override
    public List<OfferEvents> getEventsByName(Long id) {
        List<OfferEvents> offerEventsList = offerEventsRepository.findByStreamingService_AppUser_Id(id);
        return offerEventsList;
    }

    @Override
    public List<DemographicGroup> getDemographicGroups() {
        List<DemographicGroup> demographicGroupList = demographicGroupRepository.findAll();
        return demographicGroupList;
    }

    @Override
    public void joinDemographicGroup(AccountUser accountUser) {
        accountRepository.save(accountUser);
    }


    @Override
    public void addWatchEvent(AccountUser accountUser, OfferEvents offerEvents) {
        BigDecimal currentSpending = accountUser.getCurrentSpending();
        BigDecimal currentRevenue = null;
        BigDecimal currentSpendingDemo = accountUser.getDemographicGroup().getCurrentDemoSpending();
        if (offerEvents.getStreamingService() != null) {
            currentRevenue = offerEvents.getStreamingService().getCurrentRevenue();
        }
        if (currentSpending == null) {
            currentSpending = new BigDecimal(0);
        }
        if (currentSpendingDemo == null) {
            currentSpendingDemo = new BigDecimal(0);
        }
        if (currentRevenue == null) {
            currentRevenue = new BigDecimal(0);
        }
        if (offerEvents.getEvent().getEventType().equalsIgnoreCase("ppv")) {
            currentSpending = addBigDecimals(currentSpending, offerEvents.getViewingPrice());
            currentSpendingDemo = addBigDecimals(currentSpendingDemo, offerEvents.getViewingPrice());
            accountUser.setCurrentSpending(currentSpending);
            accountUser.getDemographicGroup().setCurrentDemoSpending(currentSpendingDemo);
            currentRevenue = addBigDecimals(currentRevenue, offerEvents.getViewingPrice());
            if (offerEvents.getStreamingService() != null) {
                offerEvents.getStreamingService().setCurrentRevenue(currentRevenue);
            }
        }
        accountRepository.save(accountUser);
        demographicGroupRepository.save(accountUser.getDemographicGroup());
        streamingServiceRepository.save(offerEvents.getStreamingService());
    }

    @Override
    public void subscribeStreamingService(AccountUser accountUser, StreamingService service) {
        BigDecimal currentSpending = accountUser.getCurrentSpending();
        BigDecimal currentSpendingDemo = accountUser.getDemographicGroup().getCurrentDemoSpending();
        BigDecimal currentRevenue = service.getCurrentRevenue();
        if (currentSpending == null) {
            currentSpending = new BigDecimal(0);
        }
        if (currentSpendingDemo == null) {
            currentSpendingDemo = new BigDecimal(0);
        }
        if (currentRevenue == null) {
            currentRevenue = new BigDecimal(0);
        }
        currentSpending = addBigDecimals(currentSpending, service.getSubscriptionPrice());
        currentSpendingDemo = addBigDecimals(currentSpendingDemo, service.getSubscriptionPrice());
        accountUser.setCurrentSpending(currentSpending);
        accountUser.getDemographicGroup().setCurrentDemoSpending(currentSpendingDemo);
        accountRepository.save(accountUser);
        demographicGroupRepository.save(accountUser.getDemographicGroup());
        currentRevenue = addBigDecimals(currentRevenue, service.getSubscriptionPrice());
        service.setCurrentRevenue(currentRevenue);
        streamingServiceRepository.save(service);
    }

    public AccountUser findAccountUser(String userName) {
        AppUser appUser = userRepository.findByUserName(userName);
        return accountRepository.findAccountUserByUser(appUser);
    }

    private BigDecimal addBigDecimals(BigDecimal a, BigDecimal b) {
        BigDecimal sum;
        sum = a.add(b);
        return sum;
    }


}
