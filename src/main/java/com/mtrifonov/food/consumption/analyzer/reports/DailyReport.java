package com.mtrifonov.food.consumption.analyzer.reports;

import java.time.LocalDate;
import java.util.List;
import com.mtrifonov.food.consumption.analyzer.dto.MealDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyReport implements Report {

    private LocalDate date;
    private List<MealDTO> meals;
    private Integer totalCalorie;
    private Boolean isNormal;
}
