package edu.gatech.streamingwars.studio;

import java.util.List;

public interface StudioService {

    void addEvent(Event event);

    List<Event> getEvents(String name);

    Studio getStudioDetails(String name);

    Event getEvent(Long eventId);

    void updateEvent(Event event);
}
