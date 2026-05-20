package edu.gatech.streamingwars.studio;

import edu.gatech.streamingwars.streamingservice.OfferEvents;
import edu.gatech.streamingwars.streamingservice.OfferEventsRepository;
import edu.gatech.streamingwars.user.AppUser;
import edu.gatech.streamingwars.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudioServiceImpl implements StudioService {

    private final EventRepository eventRepository;
    private final StudioRepository studioRepository;
    private final UserRepository userRepository;
    private final OfferEventsRepository offerEventsRepository;

    @Override
    public void addEvent(Event event) {

        eventRepository.save(event);

    }

    @Override
    public List<Event> getEvents(String name) {
        AppUser appuser = userRepository.findByUserName(name);
        Studio studio = studioRepository.findByAppUser(appuser);
        List<Event> events = eventRepository.findByStudio(studio);
        return events;
    }

    @Override
    public void updateEvent(Event event) {
        Optional<Event> newEvent = eventRepository.findById(event.getEventId());
        Event myEvent = newEvent.get();
        List<OfferEvents> offerEvents = offerEventsRepository.findOfferEventsByEvent(event);
        if(offerEvents.stream().count() > 0){
            myEvent.setDuration(event.getDuration());
        }
        else{
            myEvent.setLicenseFee(event.getLicenseFee());
            myEvent.setDuration(event.getDuration());
        }

        eventRepository.save(myEvent);
    }

    @Override
    public Event getEvent(Long eventId) {
        Optional<Event> newEvent = eventRepository.findById(eventId);
        return newEvent.get();
    }

    @Override
    public Studio getStudioDetails(String name) {
        AppUser appuser = userRepository.findByUserName(name);
        Studio studio = studioRepository.findByAppUser(appuser);
        return studio;
    }
}
