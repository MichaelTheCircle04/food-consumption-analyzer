package com.mtrifonov.food.consumption.analyzer.services;

import com.mtrifonov.food.consumption.analyzer.entities.User;

public interface CalorieCalculator {
    Integer calculate(User user);
}
