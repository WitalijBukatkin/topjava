package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import java.util.List;

public interface MealDAO {
    void update(Meal meal);
    void delete(int mealId);
    List<Meal> getAll();
    Meal getById(int mealId);
}