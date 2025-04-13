package com.mtrifonov.food.consumption.analyzer.services;

import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.mtrifonov.food.consumption.analyzer.entities.Dish;
import com.mtrifonov.food.consumption.analyzer.repos.DishRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DishService {

    private final DishRepository dishRepo;

    public Dish create(Dish dish) {
        return dishRepo.save(dish);
    }

    public Dish getById(Integer id) {
        return dishRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Dish with id: " + id + " doesn't exist"));
    }

    public Page<Dish> getAll(Pageable pageable) {
        return dishRepo.findAll(pageable);
    }
}
