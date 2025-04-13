package com.mtrifonov.food.consumption.analyzer.services;

import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import com.mtrifonov.food.consumption.analyzer.dto.MealDTO;
import com.mtrifonov.food.consumption.analyzer.dto.MealInfo;
import com.mtrifonov.food.consumption.analyzer.entities.Dish;
import com.mtrifonov.food.consumption.analyzer.entities.Meal;
import com.mtrifonov.food.consumption.analyzer.entities.User;
import com.mtrifonov.food.consumption.analyzer.reports.DailyReport;
import com.mtrifonov.food.consumption.analyzer.reports.HistoryReport;
import com.mtrifonov.food.consumption.analyzer.reports.ReportContainer;
import com.mtrifonov.food.consumption.analyzer.repos.MealRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MealService {

    private final MealRepository mealRepo;
    private final UserService userService;

    public void create(MealInfo info) {
        
        var user = User.builder().id(info.getUserId()).build();

        for (var id : info.getDishIds()) {
            var dish = Dish.builder().id(id).build();
            var meal = Meal.builder()
                .user(user)
                .dish(dish)
                .date(info.getDate())
                .time(info.getTime())
                .build();
            mealRepo.save(meal);
        }
    }
    
    public ReportContainer createSimpleReport(LocalDate date, Integer id) {

        var res = mealRepo.sumCalorie(date).get(0);
        var user = (User) res[0];
        var totalCalorie = (Integer) res[1];

        var containerBuilder = ReportContainer.builder().user(user);
        var report = DailyReport.builder()
            .date(date).totalCalorie(totalCalorie)
            .isNormal(totalCalorie <= user.getCalorieNorm())
            .build();
            
        return containerBuilder.report(report).build();
    }

    public ReportContainer createReport(LocalDate date, Integer id) {

        var records = mealRepo.findByDateAndUserId(date, id);
        var reportBuilder = DailyReport.builder().date(date);
        
        User user;
        if (records.isEmpty()) {
            user = userService.getById(id);
        } else {
            user = records.get(0).getUser();
        }

        var containerBuilder = ReportContainer.builder().user(user);

        var meals = new ArrayList<MealDTO>();
        int totalCalorie = 0;

        int i = 0;
        while (i < records.size()) {

            var record = records.get(i);
            var dishes = new ArrayList<Dish>();
            var time = record.getTime();
            var meal = MealDTO.builder()
                .time(time)
                .dishes(dishes)
                .build();

            while (time.equals(record.getTime())) {
                var dish = record.getDish();
                try {
                totalCalorie += dish.getCalorie(); }
                catch (Exception e) {
                    e.printStackTrace();
                }
                dishes.add(dish);
                i++;

                if (i < records.size()) {
                    record = records.get(i);
                } else {
                    break;
                }
            }

            meals.add(meal);
        }

        reportBuilder
            .meals(meals).totalCalorie(totalCalorie)
            .isNormal(totalCalorie <= user.getCalorieNorm());

        return containerBuilder
            .report(reportBuilder.build())
            .build();
    }

    public ReportContainer createHistoryReport(LocalDate from, LocalDate to, Integer id) {

        var records = mealRepo.findByDateBetweenAndUserId(from, to, id);
        var reportBuilder = HistoryReport.builder().from(from).to(to);

        User user;
        if (records.isEmpty()) {
            user = userService.getById(id);
        } else {
            user = records.get(0).getUser();
        }

        var containerBuilder = ReportContainer.builder().user(user);

        var reports = new ArrayList<DailyReport>(); //список отчетов за период

        int i = 0;
        while (i < records.size()) { //пока есть записи

            var record = records.get(i); 

            var date = record.getDate();
            var meals = new ArrayList<MealDTO>(); //приемы пищи за день
            int totalCalorie = 0; //калории за день

            var report = DailyReport.builder() //создаем отчет
                .date(date)
                .meals(meals)
                .build();

            while (i < records.size() && date.equals(record.getDate())) { //пока дата соответствует
                
                var dishes = new ArrayList<Dish>(); //блюда съеденые во время текущего приема пищи
                var time = record.getTime();
                var meal = MealDTO.builder() //текущий прием пищи
                    .time(time)
                    .dishes(dishes)
                    .build();

                while (date.equals(record.getDate()) && time.equals(record.getTime())) { //пока время соответствует

                    var dish = record.getDish(); //текущее блюда
                    totalCalorie += dish.getCalorie();
                    dishes.add(dish);
                    i++;

                    if (i < records.size()) {
                        record = records.get(i);
                    } else {
                        break;
                    }
                }
                
                meals.add(meal); 
            }

            report.setTotalCalorie(totalCalorie);
            report.setIsNormal(totalCalorie <= user.getCalorieNorm());
            reports.add(report);
        }
        
        reportBuilder.dailyReports(reports);
        return containerBuilder.report(reportBuilder.build()).build();
    }
}
