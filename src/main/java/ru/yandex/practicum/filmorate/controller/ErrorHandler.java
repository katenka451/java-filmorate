package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(ValidationException e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String errorText = "Некорректно задано значение поля " +
                e.getBindingResult().getFieldErrors().getFirst().getField();
        log.error(e.getMessage());
        log.error(errorText);
        return new ErrorResponse(errorText);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFoundException e) {
        String errorText = "Пользователь с id = " + e.getId().toString() + " не найден";
        log.error(errorText);
        return new ErrorResponse(errorText);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFound(FilmNotFoundException e) {
        String errorText = "Фильм с id = " + e.getId().toString() + " не найден";
        log.error(errorText);
        return new ErrorResponse(errorText);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleMPANotFound(MPANotFoundException e) {
        String errorText = "MPA с id = " + e.getId().toString() + " не найден";
        log.error(errorText);
        return new ErrorResponse(errorText);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGenreNotFound(GenreNotFoundException e) {
        String errorText = "Жанр с id = " + e.getId().toString() + " не найден";
        log.error(errorText);
        return new ErrorResponse(errorText);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String errorText = "Неверные входные данные";
        log.error(errorText);
        log.error(e.getMessage());
        return new ErrorResponse(errorText);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(Throwable e) {
        String errorText = "Произошла непредвиденная ошибка.";
        log.error(errorText);
        log.error(e.getMessage());
        return new ErrorResponse(errorText);
    }

}
