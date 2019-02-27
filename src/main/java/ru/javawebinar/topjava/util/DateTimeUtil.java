package ru.javawebinar.topjava.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil{
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final LocalDate DATE_MIN = LocalDate.of(0, 1, 1);
    public static final LocalDate DATE_MAX = LocalDate.of(3000, 1, 1);

    public static <T extends Comparable> boolean isBetween(T t, T start, T end) {
        return t.compareTo(start) >= 0 && t.compareTo(end) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseLocalDate(String source, LocalDate localDate) {
        return StringUtils.isEmpty(source) ? localDate : LocalDate.parse(source);
    }

    public static LocalTime parseLocalTime(String source, LocalTime localTime) {
        return StringUtils.isEmpty(source) ? localTime : LocalTime.parse(source);
    }
}