package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getById(Long id) {
        return filmStorage.getById(id);
    }

    public Film create(Film newFilm) {
        return filmStorage.create(newFilm);
    }

    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }

    public void delete(Long id) {
        filmStorage.delete(id);
    }

    public Film addLike(Long filmId, Long userId) {
        checkFilm(filmId);
        userService.checkUser(userId);

        filmStorage.getById(filmId).addLike(userId);
        return filmStorage.getById(filmId);
    }

    public Film deleteLike(Long filmId, Long userId) {
        checkFilm(filmId);
        userService.checkUser(userId);

        filmStorage.getById(filmId).deleteLike(userId);
        return filmStorage.getById(filmId);
    }

    public List<Film> getBestFilms(Long count) {
        return filmStorage.findAll().stream()
                .sorted((o1, o2) -> {
                    if (o1.getLikes().size() > o2.getLikes().size()) return -1;
                    else return 1;
                })
                .limit(count)
                .toList();
    }

    private void checkFilm(Long filmId) {
        if (!filmStorage.exists(filmId)) {
            throw new FilmNotFoundException(filmId);
        }
    }
}
