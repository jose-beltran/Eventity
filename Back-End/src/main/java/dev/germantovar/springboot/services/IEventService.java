package dev.germantovar.springboot.services;

import dev.germantovar.springboot.entities.Events;
import java.util.List;

public interface IEventService {
    List<Events> getAll();
    //para enviar datos
    void save(Events customer);
}

