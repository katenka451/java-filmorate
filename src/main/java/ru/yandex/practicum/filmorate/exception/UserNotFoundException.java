package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final Long id;

    public UserNotFoundException(Long id) {
        this.id = id;
    }

}
