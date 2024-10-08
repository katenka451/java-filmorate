package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    public static String FILM_BIRTHDAY = "28.12.1895";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film newFilm) {
        validateDataCreation(newFilm);

        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        validateDataUpdate(newFilm);

        Film oldFilm = films.get(newFilm.getId());

        if (newFilm.getName() != null) {
            oldFilm.setName(newFilm.getName());
        }

        if (newFilm.getDescription() != null) {
            validateDescription(newFilm.getDescription());
            oldFilm.setDescription(newFilm.getDescription());
        }

        if (newFilm.getReleaseDate() != null) {
            validateReleaseDate(newFilm.getReleaseDate());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }

        if (newFilm.getDuration() != null) {
            validateDuration(newFilm.getDuration());
            oldFilm.setDuration(newFilm.getDuration());
        }

        return oldFilm;
    }

    private void validateDataCreation(Film film) {
        log.info("Вызвана операция создания фильма {}", film.getName());
        validateDescription(film.getDescription());
        validateReleaseDate(film.getReleaseDate());
        validateDuration(film.getDuration());
    }

    private void validateDataUpdate(Film film) {
        log.info("Попытка обновления данных фильма");

        if (film.getId() == null) {
            throwError("Id должен быть указан");
        }

        if (!films.containsKey(film.getId())) {
            throwError("Фильм с id = " + film.getId() + " не найден");
        }

        log.info("Вызвана операция обновления фильма: id = {}, name = {}", film.getId(), films.get(film.getId()).getName());
    }

    private void validateDescription(String description) {
        if (description.length() > 200) {
            throwError("Максимальная длина описания - 200 символов");
        }
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        LocalDate cinemaBirthday = LocalDate.parse(FILM_BIRTHDAY, formatter);
        if (releaseDate.isBefore(cinemaBirthday)) {
            throwError("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

    private void validateDuration(Duration duration) {
        if (duration.toMinutes() < 0) {
            throwError("Продолжительность фильма должна быть положительным числом");
        }
    }

    private void throwError(String errorText) {
        log.error(errorText);
        throw new ValidationException(errorText);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
