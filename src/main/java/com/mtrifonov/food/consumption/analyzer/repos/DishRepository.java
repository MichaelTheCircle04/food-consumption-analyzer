package com.mtrifonov.food.consumption.analyzer.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mtrifonov.food.consumption.analyzer.entities.Dish;

public interface DishRepository extends JpaRepository<Dish, Integer> {
}
