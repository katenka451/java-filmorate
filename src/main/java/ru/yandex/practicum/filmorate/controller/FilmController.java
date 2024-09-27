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
        dataCreationValidation(newFilm);

        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        dataUpdateValidation(newFilm);

        Film oldFilm = films.get(newFilm.getId());

        if (newFilm.getName() != null) {
            oldFilm.setName(newFilm.getName());
        }

        if (newFilm.getDescription() != null) {
            descriptionValidation(newFilm.getDescription());
            oldFilm.setDescription(newFilm.getDescription());
        }

        if (newFilm.getReleaseDate() != null) {
            releaseDateValidation(newFilm.getReleaseDate());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }

        if (newFilm.getDuration() != null) {
            durationValidation(newFilm.getDuration());
            oldFilm.setDuration(newFilm.getDuration());
        }

        return oldFilm;
    }

    private void dataCreationValidation(Film film) {
        log.info("Вызвана операция создания фильма {}", film.getName());
        descriptionValidation(film.getDescription());
        releaseDateValidation(film.getReleaseDate());
        durationValidation(film.getDuration());
    }

    private void dataUpdateValidation(Film film) {
        log.info("Попытка обновления данных фильма");

        if (film.getId() == null) {
            throwError("Id должен быть указан");
        }

        if (!films.containsKey(film.getId())) {
            throwError("Фильм с id = " + film.getId() + " не найден");
        }

        log.info("Вызвана операция обновления фильма: id = {}, name = {}", film.getId(), films.get(film.getId()).getName());
    }

    private void descriptionValidation(String description) {
        if (description.length() > 200) {
            throwError("Максимальная длина описания - 200 символов");
        }
    }

    private void releaseDateValidation(LocalDate releaseDate) {
        LocalDate cinemaBirthday = LocalDate.parse(FILM_BIRTHDAY, formatter);
        if (releaseDate.isBefore(cinemaBirthday)) {
            throwError("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

    private void durationValidation(Duration duration) {
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
