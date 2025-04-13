package com.mtrifonov.food.consumption.analyzer.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MealInfo {

    @NotNull
    private LocalDate date;
    @NotNull
    private LocalTime time;
    @NotNull
    private Integer userId;
    @NotEmpty
    private List<Integer> dishIds;
}
