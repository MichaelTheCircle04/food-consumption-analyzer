package com.mtrifonov.food.consumption.analyzer.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mtrifonov.food.consumption.analyzer.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
