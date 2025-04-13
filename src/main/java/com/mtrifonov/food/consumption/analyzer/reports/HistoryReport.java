package com.mtrifonov.food.consumption.analyzer.reports;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryReport implements Report {

    private LocalDate from;
    private LocalDate to;
    private List<DailyReport> dailyReports;
}
