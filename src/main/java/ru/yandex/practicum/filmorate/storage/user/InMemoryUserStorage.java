package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User newUser) {
        validateDataCreation(newUser);

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User update(User newUser) {
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

    public void delete(Long id) {
        users.remove(id);
    }

    public boolean exists(Long id) {
        return users.containsKey(id);
    }

    public User getById(Long id) {
        if (!exists(id)) {
            throw new UserNotFoundException(id);
        }
        return users.get(id);
    }

    private void validateDataCreation(User user) {
        log.info("Вызвана операция создания пользователя {}", user.getLogin());
        validateLogin(user.getLogin());
        validateBirthday(user.getBirthday());

        if (users.values().stream().anyMatch(foundUser -> foundUser.getEmail().equals(user.getEmail()))) {
            throw new ValidationException("Этот e-mail уже используется");
        }
    }

    private void validateDataUpdate(User user) {
        log.info("Попытка обновления данных пользователя");

        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());
            if (!user.getEmail().equals(oldUser.getEmail()) && users.values().stream().anyMatch(foundUser -> foundUser.getEmail().equals(user.getEmail()))) {
                throw new ValidationException("E-mail " + user.getEmail() + " уже используется");
            }
        } else {
            throw new UserNotFoundException(user.getId());
        }

        log.info("Вызвана операция обновления пользователя: id = {}, name = {}", user.getId(), users.get(user.getId()).getName());
    }

    private void validateLogin(String login) {
        if (login.contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
        }
    }

    private void validateBirthday(LocalDate birthday) {
        LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneOffset.UTC);
        if (today.isBefore(birthday)) {
            throw new ValidationException("Дата рождения должна быть в прошлом");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }
}
