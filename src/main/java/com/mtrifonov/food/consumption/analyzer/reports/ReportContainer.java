package com.mtrifonov.food.consumption.analyzer.reports;

import com.mtrifonov.food.consumption.analyzer.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportContainer {

    private User user;
    private Report report;
}
