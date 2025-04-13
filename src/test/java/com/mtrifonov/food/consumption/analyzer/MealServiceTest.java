package com.mtrifonov.food.consumption.analyzer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.mtrifonov.food.consumption.analyzer.dto.MealInfo;
import com.mtrifonov.food.consumption.analyzer.reports.DailyReport;
import com.mtrifonov.food.consumption.analyzer.reports.HistoryReport;
import com.mtrifonov.food.consumption.analyzer.repos.MealRepository;
import com.mtrifonov.food.consumption.analyzer.repos.UserRepository;
import com.mtrifonov.food.consumption.analyzer.services.CalorieCalculator;
import com.mtrifonov.food.consumption.analyzer.services.MealService;
import com.mtrifonov.food.consumption.analyzer.services.UserService;
import jakarta.persistence.EntityManager;

@DataJpaTest
@ContextConfiguration(classes = 
    {
        MealRepository.class, MealService.class, 
        UserRepository.class, UserService.class,
        Application.class
    })
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class MealServiceTest {

    @MockitoBean
    CalorieCalculator calculator;
    @Autowired
    MealService service;
    @Autowired
    EntityManager manager;

    @Test
    void testCreatingReports() {
        
        var dailyReport = (DailyReport) service.createReport(LocalDate.parse("2023-10-02"), 1).getReport();
        assertTrue(dailyReport.getTotalCalorie() == 1400);
        assertTrue(dailyReport.getIsNormal());

        var simpleReport = (DailyReport) service.createSimpleReport(LocalDate.parse("2023-10-03"), 1).getReport(); 
        assertTrue(simpleReport.getTotalCalorie() == 1700);
        assertTrue(!simpleReport.getIsNormal());

        var historyReport = (HistoryReport) service.createHistoryReport(LocalDate.parse("2023-10-01"), LocalDate.parse("2023-10-03"), 1).getReport();
        var reports = historyReport.getDailyReports();
        assertTrue(reports.size() == 3);
        assertTrue(reports.get(0).getDate().equals(LocalDate.parse("2023-10-01")) && reports.get(0).getTotalCalorie() == 1500);
        assertTrue(reports.get(1).getDate().equals(LocalDate.parse("2023-10-02")) && reports.get(1).getTotalCalorie() == 1400);
        assertTrue(reports.get(2).getDate().equals(LocalDate.parse("2023-10-03")) && reports.get(2).getTotalCalorie() == 1700);
    }

    @Test
    @DirtiesContext
    void testCreateMealAndCreateReport() {

        var date = LocalDate.parse("2023-10-04");
        var time = LocalTime.parse("08:00");

        var mealInfo = new MealInfo();
        mealInfo.setDate(date);
        mealInfo.setTime(time);
        mealInfo.setUserId(1);
        mealInfo.setDishIds(List.of(2, 7));

        service.create(mealInfo);

        manager.flush();
        manager.clear();

        var report = (DailyReport) service.createReport(date, 1).getReport();
        assertTrue(report.getTotalCalorie() == 250);
        var meals = report.getMeals();
        assertTrue(meals.size() == 1);
        var dishes = meals.get(0).getDishes();
        assertTrue(
            dishes.size() == 2 && 
            dishes.get(0).getName().equals("Творог с медом") && 
            dishes.get(1).getName().equals("Фрукты (банан)")
        );
    }
}
