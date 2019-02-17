package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryMealDAO;
import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealDAO mealDAO = new InMemoryMealDAO(){{
        update(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        update(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        update(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        update(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        update(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        update(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }};

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if("delete".equalsIgnoreCase(action)){
            log.debug("delete of meal");

            mealDAO.delete(Integer.valueOf(request.getParameter("id")));

            log.debug("forward to ListMeal");
            response.sendRedirect("meal");
        }
        else if("insert".equalsIgnoreCase(action)){
            log.debug("forward to meal");

            request.getRequestDispatcher("/meal.jsp").forward(request, response);
        }
        else if("edit".equalsIgnoreCase(action)){
            log.debug("forward to meal");

            Integer id = Integer.valueOf(request.getParameter("id"));

            Meal meal = mealDAO.getById(id);
            request.setAttribute("id", id);
            request.setAttribute("dateTime", meal.getDateTime());
            request.setAttribute("description", meal.getDescription());
            request.setAttribute("calories", meal.getCalories());

            request.getRequestDispatcher("/meal.jsp").forward(request, response);
        }
        else {
            log.debug("forward to ListMeal");

            List<MealWithExceed> mealsWithExceeded = MealsUtil.getFilteredWithExceeded(mealDAO.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("meals", mealsWithExceeded);
            request.getRequestDispatcher("/listMeal.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("update meal");

        request.setCharacterEncoding("UTF-8");

        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if(!request.getParameter("id").equals("")) {
            meal.setId(Integer.parseInt(request.getParameter("id")));
        }
        mealDAO.update(meal);

        log.debug("forward to ListMeal");
        response.sendRedirect("meal");
    }
}