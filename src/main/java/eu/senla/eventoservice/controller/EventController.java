package eu.senla.eventoservice.controller;

import eu.senla.eventoservice.dto.ArtistModelDto;
import eu.senla.eventoservice.dto.EventModelDto;
import eu.senla.eventoservice.service.ArtistServiceInterface;
import eu.senla.eventoservice.service.EventServiceInterface;
import eu.senla.eventoservice.service.impl.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class EventController {

    private final ArtistServiceInterface artistService;

    private final EventServiceInterface eventService;

    @GetMapping()
    private String getStartPage(Model model) {

        List<EventModelDto> eventsDtoList = eventService.getTopSixEvents();
        model.addAttribute("events", eventsDtoList);

        List<ArtistModelDto> artistModelList = artistService.getTopThree();
        model.addAttribute("artists", artistModelList);

        return "index.html";
    }

    @GetMapping("events")
    private String getAllEvents(Model model) {
        List<EventModelDto> eventModelList = eventService.getAllEvents();
        model.addAttribute("events", eventModelList);

        String informationString = "THERE IS " + eventModelList.size() + " EVENTS!";
        model.addAttribute("count", informationString);

        return "events.html";
    }

    @GetMapping("events/{eventId}")
    private String getEventById(Model model,
                                @PathVariable Long eventId) {
        EventModelDto eventModelDto = eventService.getEventById(eventId);

        model.addAttribute("event", eventModelDto);

        return "event.html";
    }

}
