package com.mtrifonov.food.consumption.analyzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.mtrifonov.food.consumption.analyzer.controller.MainController;
import com.mtrifonov.food.consumption.analyzer.repos.DishRepository;
import com.mtrifonov.food.consumption.analyzer.repos.MealRepository;
import com.mtrifonov.food.consumption.analyzer.repos.UserRepository;
import com.mtrifonov.food.consumption.analyzer.services.DishService;
import com.mtrifonov.food.consumption.analyzer.services.HarrisBenedictCalculator;
import com.mtrifonov.food.consumption.analyzer.services.MealService;
import com.mtrifonov.food.consumption.analyzer.services.UserService;
import jakarta.transaction.Transactional;

@WebMvcTest(controllers = MainController.class)
@AutoConfigureDataJpa
@ContextConfiguration(classes = 
    {
        DishRepository.class, MealRepository.class,
        UserRepository.class, HarrisBenedictCalculator.class,
        DishService.class, MealService.class, UserService.class,
        Application.class
    })
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class MainControllerTest {

    MockMvc mvc;
    
    @BeforeEach
    void setup(WebApplicationContext ctx) {
        mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    void userRegistration_validRequest() throws Exception {

        var content = 
                    """
                    {
                        "name": "Sofia Falcone",
                        "email": "s.falcone@example.com",
                        "gender": "FEMALE",
                        "age": 30,
                        "height": 160,
                        "weight": 45,
                        "goal": "MAINTENANCE"
                    }
                    """;
        mvc.perform(
            post("/register").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                    status().isCreated(),
                    header().string("location", "http://localhost:8080/user/2")
                );
    }

    @Test
    void getUser_userExists() throws Exception {

        mvc.perform(get("/user/1"))
            .andExpectAll(
                status().isOk(), 
                jsonPath("$.name", is("Иван Иванов"))
            );
    }

    @Test
    @Transactional
    void createDish_validRequest() throws Exception {

        var content = 
                    """
                    {
                        "name": "Булгур (вареный)",
                        "calorie": 105,
                        "proteins": 4.2,
                        "fats": 0.4,
                        "carbohydrates": 21.2
                    }
                    """;

        mvc.perform(
            post("/dish/create").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                    status().isCreated(),
                    header().string("location", "http://localhost:8080/dish/9")
                );
    }

    @Test
    void getDish_dishExists() throws Exception {

        mvc.perform(get("/dish/7"))
            .andExpectAll(
                status().isOk(), 
                jsonPath("$.name", is("Фрукты (банан)"))
            );
    }

    @Test
    void getAllDishes() throws Exception {

        var request = get("/dish")
            .param("page", "0")
            .param("size", "5")
            .param("sort", "calorie,proteins");

        mvc.perform(request)
            .andExpectAll(
                status().isOk(),
                jsonPath("$.content.length()", is(5)),
                jsonPath("$.content[0].name", is("Фрукты (банан)")),
                jsonPath("$.content[4].name", is("Куриная грудка на гриле"))
            );
    }

    //Not valid cases
    @Test
    void getUser_userNotExists() throws Exception {

        mvc.perform(get("/user/3"))
            .andExpectAll(
                status().isBadRequest(), 
                jsonPath("$.exception", is("User with id: 3 doesn't exist"))
            );
    }

    @Test
    void getDish_dishNotExists() throws Exception {

        mvc.perform(get("/dish/10"))
            .andExpectAll(
                status().isBadRequest(),
                jsonPath("$.exception", is("Dish with id: 10 doesn't exist"))
            );
    }

    @Test
    void getSimpleReport_invalidBody() throws Exception {

        var request = get("/report/simple/1");

        mvc.perform(request)
            .andExpectAll(
                status().isBadRequest(),
                jsonPath("$.exception", containsString("'date' for method parameter type LocalDate is not present"))
            );
    }

    @Test
    void getDailyReport_invalidBody() throws Exception {

        var request = get("/report/daily/1");

        mvc.perform(request)
            .andExpectAll(
                status().isBadRequest(),
                jsonPath("$.exception", containsString("'date' for method parameter type LocalDate is not present"))
            );
    }

    @Test
    void getHistoryReport_invalidBody() throws Exception {

        var request = get("/report/history/1")
            .param("from", "2023-10-03")
            .param("to", "2023-10-01"); 

        mvc.perform(request)
            .andExpectAll(
                status().isBadRequest(), 
                jsonPath("$.exception", is("The FROM date must not be later than the TO date"))
            ); 
    }

    @Test
    void userRegistrationRequest_invalidBody() throws Exception {

        var reuqestBuilder = post("/register").contentType(MediaType.APPLICATION_JSON);

        var content = //Name is blank
                    """
                    {
                        "name": "",
                        "email": "s.falcone@example",
                        "gender": "FEMALE",
                        "age": 30,
                        "height": 160,
                        "weight": 45,
                        "goal": "MAINTENANCE"
                    }
                    """;

        mvc.perform(reuqestBuilder.content(content))
            .andExpectAll(
                status().isBadRequest(), 
                jsonPath("$.exception").value(containsString("must not be blank")));

        content = //email not valid
                """
                {
                    "name": "Sofia Falcone",
                    "email": "s.falcone?example.com",
                    "gender": "FEMALE",
                    "age": 30,
                    "height": 160,
                    "weight": 45,
                    "goal": "MAINTENANCE"
                }
                """;

        mvc.perform(reuqestBuilder.content(content))
            .andExpectAll(
                status().isBadRequest(), 
                jsonPath("$.exception").value(containsString("must be a well-formed email address")));

        content = //illegal gender
                """
                {
                    "name": "Sofia Falcone",
                    "email": "s.falcone@example.com",
                    "gender": "HELISEXUAL",
                    "age": 30,
                    "height": 160,
                    "weight": 45,
                    "goal": "MAINTENANCE"
                }
                """;

        mvc.perform(reuqestBuilder.content(content))
            .andExpectAll(
                status().isBadRequest(), 
                jsonPath("$.exception").value(containsString("not one of the values accepted for Enum class")));

        content = //illegal age
                """
                {
                    "name": "Sofia Falcone",
                    "email": "s.falcone@example.com",
                    "gender": "FEMALE",
                    "age": 130,
                    "height": 160,
                    "weight": 45,
                    "goal": "MAINTENANCE"
                }
                """;

        mvc.perform(reuqestBuilder.content(content))
                .andExpectAll(
                    status().isBadRequest(), 
                    jsonPath("$.exception").value(containsString("must be less than or equal to 100")));

        content = //illegal height
                """
                {
                    "name": "Sofia Falcone",
                    "email": "s.falcone@example.com",
                    "gender": "FEMALE",
                    "age": 30,
                    "height": 240,
                    "weight": 45,
                    "goal": "MAINTENANCE"
                }
                """;
        
        mvc.perform(reuqestBuilder.content(content))
                .andExpectAll(
                    status().isBadRequest(), 
                    jsonPath("$.exception").value(containsString("must be less than or equal to 230")));

        content = //illegal weight
                """
                {
                    "name": "Sofia Falcone",
                    "email": "s.falcone@example.com",
                    "gender": "FEMALE",
                    "age": 30,
                    "height": 160,
                    "weight": 250,
                    "goal": "MAINTENANCE"
                }
                """;

        mvc.perform(reuqestBuilder.content(content))
                .andExpectAll(
                    status().isBadRequest(), 
                    jsonPath("$.exception").value(containsString("must be less than or equal to 200")));

        content = //illegal weight
                """
                {
                    "name": "Sofia Falcone",
                    "email": "s.falcone@example.com",
                    "gender": "FEMALE",
                    "age": 30,
                    "height": 160,
                    "weight": 45,
                    "goal": "FUCKTHEWORLD"
                }
                """;

        mvc.perform(reuqestBuilder.content(content))
            .andExpectAll(
                status().isBadRequest(), 
                jsonPath("$.exception").value(containsString("not one of the values accepted for Enum class")));
    }

    @Test
    void createDish_invalidBody() throws Exception {

        var requestBuilder = post("/dish/create").contentType(MediaType.APPLICATION_JSON);
        var content = 
                    """
                    {
                        "name": "",
                        "calorie": 105,
                        "proteins": 4.2,
                        "fats": 0.4,
                        "carbohydrates": 21.2
                    }
                    """;

        mvc.perform(requestBuilder.content(content))
            .andExpectAll(
                status().isBadRequest(),
                jsonPath("$.exception", containsString("must not be blank"))
            );
        
        content = 
                """
                {
                    "name": "Булгур (вареный)",
                    "calorie": null,
                    "proteins": 4.2,
                    "fats": 0.4,
                    "carbohydrates": 21.2
                }
                """;
        
        mvc.perform(requestBuilder.content(content))
                .andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.exception", containsString("must not be null"))
                );

        content = 
                """
                {
                    "name": "Булгур (вареный)",
                    "calorie": 105,
                    "proteins": null,
                    "fats": 0.4,
                    "carbohydrates": 21.2
                }
                """;
        
        mvc.perform(requestBuilder.content(content))
                .andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.exception", containsString("must not be null"))
                );

        content = 
                """
                {
                    "name": "Булгур (вареный)",
                    "calorie": 105,
                    "proteins": 4.2,
                    "fats": null,
                    "carbohydrates": 21.2
                }
                """;
        
        mvc.perform(requestBuilder.content(content))
                .andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.exception", containsString("must not be null"))
                );

        content = 
                """
                {
                    "name": "Булгур (вареный)",
                    "calorie": 105,
                    "proteins": 4.2,
                    "fats": 0.4,
                    "carbohydrates": null
                }
                """;
        
        mvc.perform(requestBuilder.content(content))
                .andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.exception", containsString("must not be null"))
                );
    }
}
