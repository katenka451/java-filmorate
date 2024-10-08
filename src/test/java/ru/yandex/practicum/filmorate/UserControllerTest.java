package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Test
    void validateUserOk() {
        User user = User.builder()
                .id(1L)
                .email("johndoe@gmail.com")
                .login("doe1951")
                .birthday(LocalDate.parse("01.04.1951", formatter))
                .build();

        assertDoesNotThrow(() -> userController.create(user), "Пользователь не создан");
    }

    @Test
    void validateUserFail() {
        User user = User.builder()
                .id(1L)
                .email("johnnydoe@gmail.com")
                .login("johnny 1951")
                .birthday(LocalDate.parse("01.04.1951", formatter))
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertTrue(exception.getMessage().contains("Логин не должен содержать"));

        user.setLogin("johnny1951");
        LocalDate wrongBirthday = LocalDate.parse("01.04.2951", formatter);
        user.setBirthday(wrongBirthday);

        exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertTrue(exception.getMessage().contains("Дата рождения должна быть"));
    }
}
