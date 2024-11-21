package dev.germantovar.springboot.services;

import dev.germantovar.springboot.entities.City;
import dev.germantovar.springboot.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService implements ICityService{

    @Autowired
    private CityRepository repository;

    @Override
    public List<City> getAll(){
        return (List<City>) repository.findAll();
    }
}
