package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class FilmNotFoundException extends RuntimeException {
    private final Long id;

    public FilmNotFoundException(Long id) {
        this.id = id;
    }
}