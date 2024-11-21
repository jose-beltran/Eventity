package dev.germantovar.springboot.controllers;

import dev.germantovar.springboot.entities.Events;
import dev.germantovar.springboot.repository.EventRepository;
import dev.germantovar.springboot.services.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventController {

    @Autowired
    private IEventService service;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("eventos")
    public List<Events> getAll() {return service.getAll();}

    @PostMapping("eventos")
    public void save(@RequestBody Events events) {
        service.save(events);
    }
}
