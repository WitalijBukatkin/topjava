package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal meal = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
        Meal created = service.create(meal, USER_ID);
        meal.setId(created.getId());
        assertMatch(service.getAll(USER_ID), meal3, meal2, meal1, created);
    }

    @Test
    public void get() {
        Meal actual = service.get(meal2_id, USER_ID);
        assertMatch(actual, meal2);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(meal1_id, ADMIN_ID);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, meal3, meal2, meal1);
    }

    @Test
    public void update() {
        Meal meal = new Meal(meal6);
        meal.setDescription("TEST");
        meal.setCalories(200);
        service.update(meal, ADMIN_ID);
        assertMatch(service.get(meal6_id, ADMIN_ID), meal);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal meal = new Meal(meal6);
        meal.setDescription("TEST");
        meal.setCalories(200);
        service.update(meal, USER_ID);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> mealsActual = service.getBetweenDateTimes(
                LocalDateTime.of(2015, Month.MAY, 31, 11, 0),
                LocalDateTime.of(2015, Month.MAY, 31, 13, 0), ADMIN_ID);

        assertMatch(mealsActual, meal5);
    }

    @Test
    public void delete() {
        service.delete(meal5_id, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), meal6, meal4);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(meal1_id, ADMIN_ID);
    }
}