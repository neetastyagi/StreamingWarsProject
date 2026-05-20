package edu.gatech.streamingwars.studio;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudioController {

    private final StudioService studioService;

    private String userName = "";

    @GetMapping("/studio")
    public String showStudioForm(Model model, Principal principal, Authentication auth) {
        if (auth.getAuthorities().iterator().next().getAuthority().equalsIgnoreCase("studio")) {
            userName = principal.getName();
            List<Event> events = studioService.getEvents(userName);
            Studio studioCalc = studioService.getStudioDetails(userName);
            model.addAttribute("event", new Event());
            model.addAttribute("userName", userName);
            model.addAttribute("events", events);
            model.addAttribute("studioCalc", studioCalc);
        }
        return "studio";
    }

    @PostMapping("/studio")
    public String addEvent(Event event) {
        if (event.getEventId() == null) {
            Studio studio = studioService.getStudioDetails(userName);
            event.setStudio(studio);
            studioService.addEvent(event);
        } else {
            studioService.updateEvent(event);
        }
        return "redirect:/studio";
    }

    @RequestMapping(path = {"/studio", "/studio/{eventId}"})
    public String showUpdateEvent(Model model, @PathVariable("eventId") Long eventId, Principal principal) {
        userName = principal.getName();
        List<Event> events = studioService.getEvents(userName);
        Event event = studioService.getEvent(eventId);
        Studio studioCalc = studioService.getStudioDetails(userName);
        model.addAttribute("event", event);
        model.addAttribute("userName", userName);
        model.addAttribute("events", events);
        model.addAttribute("studioCalc", studioCalc);
        return "studio";
    }

}
