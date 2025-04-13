package com.mtrifonov.food.consumption.analyzer.services;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.mtrifonov.food.consumption.analyzer.entities.User;
import com.mtrifonov.food.consumption.analyzer.repos.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final CalorieCalculator calculator;

    public User register(User user) {
        user.setCalorieNorm(calculator.calculate(user));
        return repo.save(user);
    }

    public User getById(Integer id) {

        return repo.findById(id)
            .orElseThrow(
                () -> new NoSuchElementException("User with id: " + id + " doesn't exist")
            );
    }
}
