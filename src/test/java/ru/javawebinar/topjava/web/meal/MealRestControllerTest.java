package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;

class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + "/";

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void testGetAll() throws Exception {
        List<MealTo> expected = MealsUtil.getWithExcess(MEALS, SecurityUtil.authUserCaloriesPerDay());

        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(expected));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(SecurityUtil.authUserId()), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    void testCreateWithLocation() throws Exception {
        Meal expected = new Meal(LocalDateTime.now(), "Test dinner", 300);

        ResultActions actions = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Meal returned = readFromJson(actions, Meal.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);

        List<Meal> expectedGetAll = new ArrayList<>(MEALS);
        expectedGetAll.add(0, expected);

        assertIterableEquals(expectedGetAll, mealService.getAll(SecurityUtil.authUserId()));
    }

    @Test
    void testUpdate() throws Exception {
        Meal updated = new Meal(MEAL1);
        updated.setDescription("Updated description");

        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(mealService.get(MEAL1_ID, SecurityUtil.authUserId()), updated);
    }

    @Test
    void testGetBetween() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.of(2015, 5, 31, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2015, 6, 1, 23, 59, 59);

        ResultActions action = mockMvc.perform(get(REST_URL + "between?" +
                "startDate=" + startDateTime.toLocalDate() +
                "&endDate=" + endDateTime.toLocalDate() +
                "&startTime=" + endDateTime.toLocalTime() +
                "&endTime=" + endDateTime.toLocalTime()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<MealTo> actual = readListFromJsonMvcResult(action.andReturn(), MealTo.class);

        List<MealTo> expected = MealsUtil.getFilteredWithExcess(List.of(MEAL6, MEAL5, MEAL4), SecurityUtil.authUserCaloriesPerDay(),
                startDateTime.toLocalTime(), endDateTime.toLocalTime());

        assertIterableEquals(expected, actual);
    }
}