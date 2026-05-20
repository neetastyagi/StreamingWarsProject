package edu.gatech.streamingwars.streamingservice;

import edu.gatech.streamingwars.studio.Event;
import edu.gatech.streamingwars.studio.Studio;
import edu.gatech.streamingwars.studio.StudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StreamingServiceController {

    private final StreamingServiceService streamingServiceService;
    private final StudioService studioService;
    private StreamingService streamingService;
    private List<Studio> studioList;
    private List<OfferEvents> offerEventsList;
    private List<Event> eventList = new ArrayList<>();
    private Studio studioSelected = new Studio();
    private Event eventSelected = new Event();
    private OfferEvents updatedOfferedEvent = new OfferEvents();

    String userName = "";
    BigDecimal licenseFee = BigDecimal.valueOf(0.0);

    @GetMapping("/streamingservice")
    public String showStreamingServiceForm(Model model, Principal principal, Authentication auth) {
        if (auth.getAuthorities().iterator().next().getAuthority().equalsIgnoreCase("streaming service")) {
            userName = principal.getName();
            streamingService = streamingServiceService.findStreamingServiceUser(userName);
            studioList = streamingServiceService.getStudios();
            offerEventsList = streamingServiceService.getOfferEventsList(userName);

            model.addAttribute("userName", userName);
            model.addAttribute("streamingService", streamingService);
            model.addAttribute("studios", studioList);
            model.addAttribute("offeredEvents", offerEventsList);
            model.addAttribute("updatedOfferedEvent", updatedOfferedEvent);
            model.addAttribute("eventList", eventList);
            model.addAttribute("licenseFee", licenseFee);
            model.addAttribute("studioSelected", studioSelected);
            model.addAttribute("eventSelected", eventSelected);
        }
        return "streamingservice";
    }

    @PostMapping("/streamingservice")
    public String updateStreamingService(StreamingService streamingService) {
        streamingServiceService.updateStreamingService(streamingService, userName);
        return "redirect:/streamingservice";
    }

    @PostMapping("/streamingservice/subscriptionPrice")
    public String setSubscriptionFee(StreamingService streamingService) {
        streamingServiceService.updateSubscriptionPrice(streamingService, userName);
        return "redirect:/streamingservice";
    }

    @PostMapping("/streamingservice/selectStudio")
    public String getSelectedStudioEvents(Studio studio) {
        eventList.clear();
        studioSelected = new Studio();
        String studioUserName = studio.getAppUser().getUserName();
        studioSelected = studioService.getStudioDetails(studioUserName);
        eventList = studioService.getEvents(studioUserName);
        licenseFee = BigDecimal.valueOf(0.0);
        return "redirect:/streamingservice";
    }

    @PostMapping("/streamingservice/selectEvent")
    public String getSelectedEventLicenseFee(Event event) {
        eventSelected = studioService.getEvent(event.getEventId());
        licenseFee = eventSelected.getLicenseFee();
        return "redirect:/streamingservice";
    }

    @PostMapping("/streamingservice/payLicenseFee")
    public String payLicenseFee()
    {
        if(eventSelected != null) {
            OfferEvents offerEvents = new OfferEvents();
            offerEvents.setEvent(eventSelected);
            offerEvents.setViewingPrice(BigDecimal.valueOf(0.0));
            StreamingService service = streamingServiceService.findStreamingServiceUser(userName);
            offerEvents.setStreamingService(service);

            boolean addEvent = true;
            List<OfferEvents> offerEventsList = streamingServiceService.getOfferEventsList(userName);
            for (OfferEvents event : offerEventsList) {
                if ((event.getEvent().getEventName()).equalsIgnoreCase(offerEvents.getEvent().getEventName()) &&
                        event.getEvent().getYearProduced() == offerEvents.getEvent().getYearProduced()) {
                    addEvent = false;
                    break;
                }
            }
            if (addEvent) {
                streamingServiceService.addOfferEvent(offerEvents);
                streamingServiceService.payLicenseFeeToStudio(userName, studioSelected, licenseFee);
            }
        }
        return "redirect:/streamingservice";
    }

    @RequestMapping(path = {"/streamingservice", "/streamingservice/changePrice/{eventId}"})
    public String updatePpvViewingPrice(@PathVariable("eventId") Long eventId, OfferEvents updatedOfferedEvent) {
        streamingServiceService.updateOfferedEventViewingPrice(eventId, updatedOfferedEvent.getViewingPrice());
        return "redirect:/streamingservice";
    }


    @RequestMapping(path = {"/streamingservice", "/streamingservice/{eventId}"})
    public String retractMovie(@PathVariable("eventId") Long eventId) {
        streamingServiceService.deleteOfferedEvent(eventId);
        return "redirect:/streamingservice";
    }
}