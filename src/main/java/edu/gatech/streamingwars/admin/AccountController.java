package edu.gatech.streamingwars.account;

import edu.gatech.streamingwars.admin.DemographicGroup;
import edu.gatech.streamingwars.streamingservice.OfferEvents;
import edu.gatech.streamingwars.streamingservice.StreamingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private AccountUser accountUser;
    private List<StreamingService> streamingServiceList;
    private List<DemographicGroup> demographicGroupList;
    private List<StreamingService> subscribedServiceList;
    private List<OfferEvents> offerEventsList;
    private List<OfferEvents> movieList ;
    private List<OfferEvents> ppvList;
    private String userName ;
    private Boolean disableButton = false;

    @GetMapping("/account")
    public String populateAccountView(Model model, Principal principal, Authentication auth) {
        if (auth.getAuthorities().iterator().next().getAuthority().equalsIgnoreCase("account")) {
            List<StreamingService> streamingServices = new ArrayList<>();
            userName = principal.getName();
            accountUser = accountService.findAccountUser(userName);
            streamingServiceList = accountService.getStreamingServices();
            for(StreamingService streamingService : streamingServiceList){
                if(streamingService.getSubscriptionPrice() != null){
                    streamingServices.add(streamingService);
                }
            }
            demographicGroupList = accountService.getDemographicGroups();
            subscribedServiceList = (List<StreamingService>) accountUser.getStreamingServices();
            if (accountUser.getWatchEvents() != null && !accountUser.getWatchEvents().isEmpty()) {
                disableButton = true;
            } else {
                disableButton = false;
            }
            if (subscribedServiceList != null) {
                streamingServices.removeAll(subscribedServiceList);
            }
            List<OfferEvents> movieList = new ArrayList<>();
            List<OfferEvents> ppvList = new ArrayList<>();
            model.addAttribute("movieList", movieList);
            model.addAttribute("ppvList", ppvList);
            model.addAttribute("offerEvents", new OfferEvents());
            model.addAttribute("streamingService", new StreamingService());
            model.addAttribute("streamingServices", streamingServices);
            model.addAttribute("demoGroups", demographicGroupList);
            model.addAttribute("userName", userName);
            model.addAttribute("subscribedServices", subscribedServiceList);
            model.addAttribute("accountUser", accountUser);
            model.addAttribute("watchEvents", accountUser.getWatchEvents());
            model.addAttribute("disableButton", disableButton);

        }
        return "account";
    }

    @GetMapping("/account/movie/{subscribedService}")
    public String getMovieEvents(Model model, @PathVariable("subscribedService") Long id) {

        findEvents(model, id, "movie");
        return  "account::moviefrag";

    }

    @GetMapping("/account/ppv/{subscribedService}")
    public String getPpvEvents(Model model, @PathVariable("subscribedService") Long id) {
        findEvents(model, id, "ppv");
        return "account::ppvfrag";

    }

    @PostMapping("/account/streaming")
    public String subscribeStreamingService(StreamingService streamingService) {
        StreamingService service = new StreamingService();
        for (StreamingService streamingService1 : streamingServiceList) {
            if (streamingService1.getId().equals(streamingService.getId())) {
                service = streamingService1;
                accountUser.getStreamingServices().add(streamingService1);
            }
        }

        accountService.subscribeStreamingService(accountUser, service);
        return "redirect:/account";
    }

    @PostMapping("/account/demoGroup")
    public String joinDemographicGroup(AccountUser accountUserData) {
        accountUser.setDemographicGroup(accountUserData.getDemographicGroup());
        accountService.joinDemographicGroup(accountUser);
        return "redirect:/account";
    }

    @PostMapping("/account/watchEvent")
    public String addWatchEvent(OfferEvents offerEvent) {
        OfferEvents offerEvents = new OfferEvents();
        for (OfferEvents offerEvents1 : offerEventsList) {
            if (offerEvents1.getId().equals(offerEvent.getId())) {
                offerEvents = offerEvents1;
                accountUser.getWatchEvents().add(offerEvents1);
            }
        }
        accountService.addWatchEvent(accountUser, offerEvents);
        movieList.remove(offerEvent);
        return "redirect:/account";
    }

    private void findEvents(Model model, Long id, String type) {
        movieList = new ArrayList<>();
        ppvList = new ArrayList<>();
        offerEventsList = accountService.getEventsByName(id);
        if (accountUser.getWatchEvents() != null||!accountUser.getWatchEvents().isEmpty()) {
            offerEventsList.removeAll(accountUser.getWatchEvents());
        }
        for (OfferEvents offerEvent : offerEventsList) {
            if (type.equals("movie")) {
                if (offerEvent.getEvent().getEventType().equalsIgnoreCase("movie")) {
                    movieList.add(offerEvent);
                }
            } else if (type.equals("ppv")) {
                if (offerEvent.getEvent().getEventType().equalsIgnoreCase("ppv")) {
                    ppvList.add(offerEvent);
                }
            }

        }
        model.addAttribute("offerEvents", new OfferEvents());
        model.addAttribute("movieList", movieList);
        model.addAttribute("movie", new OfferEvents());
        model.addAttribute("ppvList", ppvList);
        model.addAttribute("ppv", new OfferEvents());
    }

}
