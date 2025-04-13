package com.mtrifonov.food.consumption.analyzer.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_storage")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="user_seq")
    @SequenceGenerator(name="user_seq", sequenceName="user_storage_user_id_seq", initialValue = 2)
    @Column(name = "user_id")
    private Integer id;
    @NotBlank
    private String name;
    @NotNull @Email
    private String email;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @NotNull @Min(12) @Max(100)
    private Integer age;
    @NotNull @Min(130) @Max(230)
    private Integer height;
    @NotNull @Min(20) @Max(200)
    private Integer weight;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Goal goal;
    @Column(name = "calorie_norm")
    private Integer calorieNorm;

    public enum Gender {
        MALE,
        FEMALE
    }
    
    public enum Goal {
        LOSS,
        MAINTENANCE,
        GAIN
    }
}
