package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.web.SecurityUtil.*;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        return getWithExceeded(service.getAll(), authUserCaloriesPerDay());
    }

    public List<MealWithExceed> getFilteredByDateAndTime(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime){
        log.info("get filtered list");

        return getWithExceeded(service.getAll().stream()
                .filter(m -> DateTimeUtil.isBetween(m.getTime(), startTime, endTime) &&
                             DateTimeUtil.isBetween(m.getDate(), startDate, endDate))
                .collect(Collectors.toList()),
                authUserCaloriesPerDay());
    }

    public Meal get(int id){
        log.info("get {}", id);
        return service.get(id);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        meal.setUserId(authUserId());
        return service.create(meal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        meal.setUserId(authUserId());
        service.update(meal);
    }
}