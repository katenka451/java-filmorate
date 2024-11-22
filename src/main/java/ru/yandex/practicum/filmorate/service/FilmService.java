package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.MPANotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    public static String FILM_BIRTHDAY = "1895-12-28";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final FilmRepository filmRepository;
    private final LikeService likeService;
    private final UserService userService;
    private final MPAService mpaService;
    private final GenreService genreService;

    public List<FilmDto> findAll() {
        return filmRepository.findAll().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto getById(Long id) {
        var filmDto = filmRepository.getById(id)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new FilmNotFoundException(id));
        try {
            filmDto.setMpa(mpaService.getById(filmDto.getMpa().getId()));
        } catch (MPANotFoundException ignored) {
        }    // no MPA

        filmDto.setGenres(genreService.getByFilmId(filmDto.getId()));

        return filmDto;
    }

    public FilmDto create(FilmDto newFilm) {
        validateDataCreation(newFilm);
        newFilm.setGenres(getUniqueGenres(newFilm.getGenres()));
        return FilmMapper.mapToFilmDto(filmRepository.create(FilmMapper.mapDtoToFilm(newFilm)));
    }

    public FilmDto update(FilmDto updFilm) {
        validateDataUpdate(updFilm);

        return FilmMapper.mapToFilmDto(filmRepository.update(FilmMapper.mapDtoToFilm(updFilm)));
    }

    public boolean delete(Long id) {
        return filmRepository.delete(id);
    }

    public boolean addLike(Long filmId, Long userId) {
        return likeService.create(getById(filmId).getId(), userService.getById(userId).getId());
    }

    public boolean deleteLike(Long filmId, Long userId) {
        return likeService.delete(filmId, userId);
    }

    public List<FilmDto> getBestFilms(Long count) {
        return filmRepository.getBestFilms(count).stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    private void validateDataCreation(FilmDto film) {
        log.info("Вызвана операция создания фильма {}", film.getName());
        validateDescription(film.getDescription());
        validateReleaseDate(LocalDate.parse(film.getReleaseDate(), formatter));
        validateDuration(film.getDuration());
    }

    private void validateDataUpdate(FilmDto film) {
        log.info("Вызвана операция обновления фильма");

        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (getById(film.getId()) == null) {
            throw new FilmNotFoundException(film.getId());
        }
        if (film.getDescription() != null && !film.getDescription().isEmpty()) {
            validateDescription(film.getDescription());
        }
        if (film.getReleaseDate() != null && !film.getReleaseDate().isEmpty()) {
            validateReleaseDate(LocalDate.parse(film.getReleaseDate(), formatter));
        }
        if (film.getDuration() != null) {
            validateDuration(film.getDuration());
        }
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

    private void validateDuration(Integer duration) {
        if (duration < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    private List<GenreDto> getUniqueGenres(List<GenreDto> genres) {
        if (genres == null) {
            return null;
        }
        return genres.stream()
                .distinct()
                .toList();
    }

}
