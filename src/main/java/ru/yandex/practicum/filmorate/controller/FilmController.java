package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    public static int COUNT = 10;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<FilmDto> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public FilmDto getById(@PathVariable Long id) {
        return filmService.getById(id);
    }

    @GetMapping("/popular")
    public List<FilmDto> getBestFilms(@RequestParam(name = "count", required = false) Long count) {
        return filmService.getBestFilms((count == null || count == 0) ? COUNT : count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto create(@Valid @RequestBody FilmDto newFilm) {
        return filmService.create(newFilm);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody FilmDto filmUpdate) {
        return filmService.update(filmUpdate);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        if (!filmService.addLike(id, userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        if (!filmService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        if (!filmService.deleteLike(id, userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
