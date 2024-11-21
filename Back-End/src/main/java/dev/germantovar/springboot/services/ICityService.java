package dev.germantovar.springboot.services;

import dev.germantovar.springboot.entities.City;
import java.util.List;

public interface ICityService {
    List<City> getAll();
}
