package dev.germantovar.springboot.repository;

import dev.germantovar.springboot.entities.Events;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Events, Long> {
}

