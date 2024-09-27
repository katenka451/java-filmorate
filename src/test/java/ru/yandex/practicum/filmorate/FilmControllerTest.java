package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTest {

    @Autowired
    private FilmController filmController;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Test
    void validateFilmOk() {
        Film film = Film.builder()
                .id(1L)
                .name("Lord of the Rings")
                .description("A trilogy of epic fantasy adventure films directed by Peter Jackson")
                .releaseDate(LocalDate.parse("10.12.2001", formatter))
                .duration(Duration.ofMinutes(558))
                .build();

        assertDoesNotThrow( () -> filmController.create(film), "Фильм не создан");
    }

    @Test
    void validateFilmFailed() {
        Film film = Film.builder()
                .id(1L)
                .name("Lord of the Rings")
                .description("A trilogy of epic fantasy adventure films directed by Peter Jackson")
                .releaseDate(LocalDate.parse("10.12.1001", formatter))
                .duration(Duration.ofMinutes(558))
                .build();

        Exception exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertTrue(exception.getMessage().contains("Дата релиза должна быть не раньше"));

        film.setReleaseDate(LocalDate.parse("10.12.2001", formatter));
        film.setDescription("A trilogy of epic fantasy adventure films directed by Peter Jackson, " +
                "based on the novel The Lord of the Rings by British author J. R. R. Tolkien. " +
                "The films are subtitled The Fellowship of the Ring (2001), The Two Towers (2002), " +
                "and The Return of the King (2003). Produced and distributed by New Line Cinema with " +
                "the co-production of WingNut Films, the films feature an ensemble cast");

        exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertTrue(exception.getMessage().contains("Максимальная длина описания"));

    }

}
