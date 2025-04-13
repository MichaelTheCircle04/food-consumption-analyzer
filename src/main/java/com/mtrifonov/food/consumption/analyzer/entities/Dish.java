package com.mtrifonov.food.consumption.analyzer.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dish")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="dish_seq")
    @SequenceGenerator(name="dish_seq", sequenceName="dish_dish_id_seq", initialValue = 9)
    @Column(name = "dish_id")
    private Integer id;
    @NotBlank
    private String name;
    @NotNull
    private Integer calorie;
    @NotNull
    private Double proteins;
    @NotNull 
    private Double fats;
    @NotNull
    private Double carbohydrates;
}
