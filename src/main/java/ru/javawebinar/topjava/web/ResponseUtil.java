package ru.javawebinar.topjava.web;

import org.springframework.validation.BindingResult;

import java.util.StringJoiner;

public class ResponseUtil {

    private ResponseUtil() {
    }

    public static String getErrorsFromBindingResult(BindingResult result) {
        StringJoiner joiner = new StringJoiner("<br>");
        result.getFieldErrors().forEach(
                fe -> {
                    String msg = fe.getDefaultMessage();
                    if (msg != null) {
                        if (!msg.startsWith(fe.getField())) {
                            msg = fe.getField() + ' ' + msg;
                        }
                        joiner.add(msg);
                    }
                });
        return joiner.toString();
    }
}
