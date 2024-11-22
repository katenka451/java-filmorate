package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class GenreNotFoundException extends RuntimeException {
    private final Long id;

    public GenreNotFoundException(Long id) {
        this.id = id;
    }
}