package com.mtrifonov.food.consumption.analyzer.services;

import static com.mtrifonov.food.consumption.analyzer.entities.User.Gender.*;
import static com.mtrifonov.food.consumption.analyzer.entities.User.Goal.*;
import org.springframework.stereotype.Component;
import com.mtrifonov.food.consumption.analyzer.entities.User;

@Component
public class HarrisBenedictCalculator implements CalorieCalculator {

    @Override
    public Integer calculate(User user) {
        
        Double result;

        if (user.getGender() == MALE) {
            result = 88.36 + (13.4 * user.getWeight()) + (4.8 * user.getHeight()) - (5.7 * user.getAge());
        } else {
            result = 447.6 + (9.2 * user.getWeight()) + (3.1 * user.getHeight()) - (4.3 * user.getAge());
        }

        if (user.getGoal() == LOSS) {
            result *= 0.85;
        } else if (user.getGoal() == GAIN) {
            result *= 1.15;
        }

        return result.intValue();
    }

}
