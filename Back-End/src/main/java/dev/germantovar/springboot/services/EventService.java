package dev.germantovar.springboot.services;

import dev.germantovar.springboot.entities.Events;
import dev.germantovar.springboot.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService implements IEventService{

    @Autowired
    private EventRepository repository;

    @Override
    public List<Events> getAll(){
        return (List<Events>) repository.findAll();
    }

    @Override
    public void save(Events events) {
        repository.save(events);
    }

}

