package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    public static String FILM_BIRTHDAY = "28.12.1895";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film create(Film newFilm) {
        validateDataCreation(newFilm);

        newFilm.setId(getNextId());
        newFilm.setLikes(new HashSet<>());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    public Film update(Film newFilm) {
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

    public void delete(Long id) {
        films.remove(id);
    }

    public boolean exists(Long id) {
        return films.containsKey(id);
    }

    public Film getById(Long id) {
        if (!exists(id)) {
            throw new FilmNotFoundException(id);
        }
        return films.get(id);
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
            throw new ValidationException("Id должен быть указан");
        }

        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException(film.getId());
        }

        log.info("Вызвана операция обновления фильма: id = {}, name = {}", film.getId(), films.get(film.getId()).getName());
    }

    private void validateDescription(String description) {
        if (description.length() > 200) {
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        LocalDate cinemaBirthday = LocalDate.parse(FILM_BIRTHDAY, formatter);
        if (releaseDate.isBefore(cinemaBirthday)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

    private void validateDuration(Duration duration) {
        if (duration.toMinutes() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
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
