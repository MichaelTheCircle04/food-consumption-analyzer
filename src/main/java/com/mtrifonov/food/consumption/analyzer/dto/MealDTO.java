package com.mtrifonov.food.consumption.analyzer.dto;

import java.time.LocalTime;
import java.util.List;
import com.mtrifonov.food.consumption.analyzer.entities.Dish;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MealDTO {
    private LocalTime time;
    private List<Dish> dishes;
}
