package com.example.kafkaconsumer.jpa;

import com.example.kafkaconsumer.entity.TurnstileEvent;
import org.springframework.data.repository.CrudRepository;

public interface TurnstileEventRepository extends CrudRepository<TurnstileEvent,Integer> {
}
