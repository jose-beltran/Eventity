package dev.germantovar.springboot.controllers;

import dev.germantovar.springboot.entities.City;
import dev.germantovar.springboot.repository.CityRepository;
import dev.germantovar.springboot.services.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CityController {

    @Autowired
    private ICityService service;

    @Autowired
    private CityRepository cityRepository;

    @GetMapping("ciudades")
    public List<City> getAll() {
        return service.getAll();
    }

}

