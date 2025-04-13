package com.mtrifonov.food.consumption.analyzer.controller;

import java.net.URI;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.mtrifonov.food.consumption.analyzer.dto.MealInfo;
import com.mtrifonov.food.consumption.analyzer.entities.Dish;
import com.mtrifonov.food.consumption.analyzer.entities.User;
import com.mtrifonov.food.consumption.analyzer.reports.ReportContainer;
import com.mtrifonov.food.consumption.analyzer.services.DishService;
import com.mtrifonov.food.consumption.analyzer.services.MealService;
import com.mtrifonov.food.consumption.analyzer.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Value("${server.name}")
    private String address;
    private final UserService userService;
    private final DishService dishService;
    private final MealService mealService;
    
    @PostMapping("/register") //Регистрация пользователя //covered
    public ResponseEntity<Void> registerUser(@Valid @RequestBody User user) {
        var registered = userService.register(user); 
        return ResponseEntity.created(URI.create("http://" + address + "/user/" + registered.getId())).build();
    }
    
    @GetMapping("/user/{id}") //Получение пользователя //covered
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getById(id));
    }
    
    @PostMapping("/dish/create") //Создание блюда //covered
    public ResponseEntity<Void> createDish(@Valid @RequestBody Dish dish) {
        var created = dishService.create(dish);
        return ResponseEntity.created(URI.create("http://" + address + "/dish/" + created.getId())).build();
    }
    
    @GetMapping("/dish/{id}") //Получение блюда //covered
    public ResponseEntity<Dish> getDish(@PathVariable Integer id) {
        return ResponseEntity.ok(dishService.getById(id));
    }
    
    @GetMapping("/dish") //Получение блюд в соответствии с пагинацией //covered
    public ResponseEntity<Page<Dish>> getAllDishes(@PageableDefault(sort = {"calorie"}) Pageable pageable) {
        return ResponseEntity.ok(dishService.getAll(pageable));
    }
    
    @PostMapping("/meal/create") //Запись о приеме пищи
    public ResponseEntity<Void> createMeal(@Valid @RequestBody MealInfo info) {
        mealService.create(info);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/report/simple/{id}") //Простой отчет
    public ResponseEntity<ReportContainer> getSimpleReport(
        @RequestParam LocalDate date, @PathVariable Integer id) {

        var report = mealService.createSimpleReport(date, id);
        return ResponseEntity.ok(report);
    }
    @GetMapping("/report/daily/{id}") //Дневной отчет
    public ResponseEntity<ReportContainer> getDailyReport(
        @RequestParam LocalDate date, @PathVariable Integer id) {

        var report = mealService.createReport(date, id);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/report/history/{id}") //Отчет за промежуток времени
    public ResponseEntity<ReportContainer> getHistoryReport(
        @Valid Dates dates, @PathVariable Integer id) {
        
        if (dates.from.compareTo(dates.to) > 0) {
            throw new IllegalArgumentException("The FROM date must not be later than the TO date");
        }

        var report = mealService.createHistoryReport(dates.from, dates.to, id);
        return ResponseEntity.ok(report);
    }

    public record Dates(@RequestParam LocalDate from, @RequestParam LocalDate to) {
    }
}
