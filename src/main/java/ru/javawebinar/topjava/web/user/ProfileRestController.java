package ru.javawebinar.topjava.web.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;

import javax.validation.Valid;
import java.net.URI;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(ProfileRestController.REST_URL)
public class ProfileRestController extends AbstractUserController {
    static final String REST_URL = "/rest/profile";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get() {
        return super.get(authUserId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        super.delete(authUserId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo, BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }

        User created;
        try {
            created = super.create(userTo);
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("email", "user.duplicateEmail",
                    "User with this email already exists");
            throw new BindException(result);
        }

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserTo userTo, BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }

        try {
            super.update(userTo, authUserId());
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("email", "user.duplicateEmail",
                    "User with this email already exists");
            throw new BindException(result);
        }
    }

    @GetMapping(value = "/text")
    public String testUTF() {
        return "Русский текст";
    }
}