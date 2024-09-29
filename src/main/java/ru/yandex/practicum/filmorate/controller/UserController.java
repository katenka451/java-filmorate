package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User newUser) {
        validateDataCreation(newUser);

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        validateDataUpdate(newUser);

        User oldUser = users.get(newUser.getId());

        // если пользователь найден, и все условия соблюдены, обновляем информацию о нем
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }

        if (newUser.getLogin() != null) {
            validateLogin(newUser.getLogin());
            oldUser.setLogin(newUser.getLogin());
        }

        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }

        if (newUser.getBirthday() != null) {
            validateBirthday(newUser.getBirthday());
            oldUser.setBirthday(newUser.getBirthday());
        }

        return oldUser;
    }

    private void validateDataCreation(User user) {
        log.info("Вызвана операция создания пользователя {}", user.getLogin());
        validateLogin(user.getLogin());
        validateBirthday(user.getBirthday());

        if (users.values().stream().anyMatch(foundUser -> foundUser.getEmail().equals(user.getEmail()))) {
            throwError("Этот e-mail уже используется");
        }
    }

    private void validateDataUpdate(User user) {
        log.info("Попытка обновления данных пользователя");

        if (user.getId() == null) {
            throwError("Id должен быть указан");
        }

        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());
            if (!user.getEmail().equals(oldUser.getEmail()) && users.values().stream().anyMatch(foundUser -> foundUser.getEmail().equals(user.getEmail()))) {
                throwError("E-mail " + user.getEmail() + " уже используется");
            }
        } else {
            throwError("Пользователь с id = " + user.getId() + " не найден");
        }

        log.info("Вызвана операция обновления пользователя: id = {}, name = {}", user.getId(), users.get(user.getId()).getName());
    }

    private void validateLogin(String login) {
        if (login.contains(" ")) {
            throwError("Логин не должен содержать пробелы");
        }
    }

    private void validateBirthday(LocalDate birthday) {
        LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneOffset.UTC);
        if (today.isBefore(birthday)) {
            throwError("Дата рождения должна быть в прошлом");
        }
    }

    private void throwError(String errorText) {
        log.error(errorText);
        throw new ValidationException(errorText);
    }

    private long getNextId() {
        long currentMaxId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }
}
