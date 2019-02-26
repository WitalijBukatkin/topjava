package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        meal.setUserId(SecurityUtil.authUserId());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }

        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id) {
        try {
            Meal meal = repository.get(id);

            if(meal.getUserId() != SecurityUtil.authUserId())
                return false;

            repository.remove(id);

            return true;
        } catch(RuntimeException e){
            return false;
        }
    }

    @Override
    public Meal get(int id) {
        try {
            Meal meal = repository.get(id);

            if(meal.getUserId() != SecurityUtil.authUserId())
                return null;

            return meal;
        } catch(RuntimeException e){
            return null;
        }
    }

    @Override
    public List<Meal> getAll() {
        return repository.values().stream()
                .filter(u -> u.getUserId() == SecurityUtil.authUserId())
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }
}