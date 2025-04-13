package com.mtrifonov.food.consumption.analyzer.repos;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import com.mtrifonov.food.consumption.analyzer.entities.Meal;

public interface MealRepository extends JpaRepository<Meal, Long> {
    @NativeQuery(value = 
        """
        SELECT 
            u.user_id, u.name, email, gender,
            age, height, weight, goal,
            calorie_norm, SUM(calorie) total_calorie
        FROM meal m
            JOIN user_storage u USING(user_id) 
            JOIN dish d USING(dish_id) 
        WHERE 
            date = ?1
        GROUP BY date
        """, sqlResultSetMapping = "SumCalorieUserMapping"
    )
    List<Object[]> sumCalorie(LocalDate date);
    @NativeQuery(value = 
        """
        SELECT 
            meal_id, date, time,
            u.user_id, u.name, email, gender,
            age, height, weight, goal, calorie_norm, 
            d.dish_id dish_id, d.name d_name, calorie, proteins, fats, carbohydrates
        FROM meal m 
            JOIN user_storage u USING(user_id) 
            JOIN dish d USING(dish_id)
        WHERE
            date = ?1 and u.user_id = ?2
        ORDER BY
            time
        """, sqlResultSetMapping = "UserDishMealMapping")
    List<Meal> findByDateAndUserId(LocalDate date, Integer userId);
    @NativeQuery(value = 
        """
        SELECT 
            meal_id, date, time,
            u.user_id, u.name, email, gender,
            age, height, weight, goal, calorie_norm, 
            d.dish_id dish_id, d.name d_name, calorie, proteins, fats, carbohydrates 
        FROM meal m 
            JOIN user_storage u USING(user_id) 
            JOIN dish d USING(dish_id) 
        WHERE 
            date BETWEEN ?1 AND ?2 AND 
            u.user_id = ?3
        ORDER BY
            date, time
        """, sqlResultSetMapping = "UserDishMealMapping"
    )
    List<Meal> findByDateBetweenAndUserId(LocalDate from, LocalDate to, Integer userId);
}
