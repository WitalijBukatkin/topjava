package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;

@Component
public class DuplicateEmailValidator implements Validator {

    @Autowired
    private UserService service;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo user = (UserTo) target;

        User userByEmail = service.getByEmail(user.getEmail());

        if (userByEmail != null && !userByEmail.getId().equals(user.getId())) {
            errors.rejectValue("email", "user.duplicateEmail",
                    "User with this email already exists");
        }
    }
}
