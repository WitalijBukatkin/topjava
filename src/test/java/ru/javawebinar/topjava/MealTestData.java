package ru.javawebinar.topjava;


import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

public class MealTestData {
    public static final int meal1_id = ADMIN_ID + 1;
    public static final int meal2_id = meal1_id + 1;
    public static final int meal3_id = meal2_id + 1;
    public static final int meal4_id = meal3_id + 1;
    public static final int meal5_id = meal4_id + 1;
    public static final int meal6_id = meal5_id + 1;

    public static final Meal meal1 = new Meal(meal1_id, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final Meal meal2 = new Meal(meal2_id, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final Meal meal3 = new Meal(meal3_id, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
    public static final Meal meal4 = new Meal(meal4_id, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal meal5 = new Meal(meal5_id, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
    public static final Meal meal6 = new Meal(meal6_id, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "roles");
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}