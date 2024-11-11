package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class MPANotFoundException extends RuntimeException {
    private final Long id;

    public MPANotFoundException(Long id) {
        this.id = id;
    }
}