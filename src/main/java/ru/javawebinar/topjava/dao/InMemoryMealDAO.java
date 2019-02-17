package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMealDAO implements MealDAO {

    private ConcurrentHashMap<Integer, Meal> meals = new ConcurrentHashMap<>();

    @Override
    public void update(Meal meal) {
        if (meal.isNew()) {
            meal.setId(meals.keySet().stream().mapToInt(i -> i).max().orElse(0) + 1);
        }
        meals.put(meal.getId(), meal);
    }

    @Override
    public void delete(int mealId) {
        meals.remove(mealId);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getById(int mealId) {
        return meals.get(mealId);
    }
}