package com.mtrifonov.food.consumption.analyzer.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FetchType;
import jakarta.persistence.FieldResult;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "meal")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "dish"})
@EqualsAndHashCode(exclude = {"user", "dish"})
@SqlResultSetMapping(
    name = "UserDishMealMapping", 
    entities = {
        @EntityResult(entityClass = Meal.class),
        @EntityResult(entityClass = User.class), 
        @EntityResult(entityClass = Dish.class, fields = {
            @FieldResult(name = "id", column = "dish_id"),
            @FieldResult(name = "name", column = "d_name"),
            @FieldResult(name = "calorie", column = "calorie"),
            @FieldResult(name = "proteins", column = "proteins"),
            @FieldResult(name = "fats", column = "fats"),
            @FieldResult(name = "carbohydrates", column = "carbohydrates")
        })
    }
)
@SqlResultSetMapping(
    name = "SumCalorieUserMapping",
    entities = {
        @EntityResult(entityClass = User.class)
        },
    columns = {
        @ColumnResult(name = "total_calorie", type = Integer.class)
    }
)
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="meal_seq")
    @SequenceGenerator(name="meal_seq", sequenceName="meal_meal_id_seq", initialValue = 19)
    @Column(name = "meal_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dish_id")
    private Dish dish;
    private LocalDate date;
    private LocalTime time;
}
