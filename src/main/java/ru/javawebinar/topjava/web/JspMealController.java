package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController {

    @Autowired
    private MealService service;

    @GetMapping("/meals")
    public String meals(Model model) {
        int userId = SecurityUtil.authUserId();

        model.addAttribute("meals",
                MealsUtil.getWithExcess(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));

        return "meals";
    }

    @PostMapping("/meals")
    public String filteredMeals(Model model,
                                @RequestParam String startDate,
                                @RequestParam String endDate,
                                @RequestParam String startTime,
                                @RequestParam String endTime) {

        int userId = SecurityUtil.authUserId();

        List<Meal> mealsDateFiltered = service.getBetweenDates(parseLocalDate(startDate),
                parseLocalDate(endDate), userId);

        model.addAttribute("meals",
                MealsUtil.getFilteredWithExcess(mealsDateFiltered,
                        SecurityUtil.authUserCaloriesPerDay(),
                        parseLocalTime(startTime), parseLocalTime(endTime)));
        return "meals";
    }

    @GetMapping("/meal")
    public String meal(Model model, @RequestParam(required = false) String id) {
        int userId = SecurityUtil.authUserId();

        if (id == null) {
            model.addAttribute("meal",
                    new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        } else {
            int idI = Integer.valueOf(id);
            model.addAttribute("meal",
                    service.get(idI, userId));
        }

        return "mealForm";
    }

    @GetMapping("/meal/delete")
    public String deleteMeal(@RequestParam String id) {
        int userId = SecurityUtil.authUserId();
        int idI = Integer.valueOf(id);

        service.delete(idI, userId);

        return "redirect:../meals";
    }

    @PostMapping("/meal")
    public String setMeal(@RequestParam(required = false) String id,
                          @RequestParam String dateTime,
                          @RequestParam String description,
                          @RequestParam String calories) {

        int userId = SecurityUtil.authUserId();

        Meal meal = new Meal(LocalDateTime.parse(dateTime), description,
                Integer.parseInt(calories));

        if (id == null) {
            service.create(meal, userId);
        } else {
            service.update(meal, userId);
        }

        return "redirect:meals";
    }
}
