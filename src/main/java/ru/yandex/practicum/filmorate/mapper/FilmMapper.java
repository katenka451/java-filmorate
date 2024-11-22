package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MPADto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {

    public static FilmDto mapToFilmDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate() != null ? film.getReleaseDate().toString() : null)
                .duration(film.getDuration())
                .mpa(film.getMpa() != null ? MPADto.builder()
                        .id(film.getMpa().getId())
                        .name(film.getMpa().getName())
                        .build() : null)
                .genres(film.getGenres() != null ? film.getGenres().stream().map(genre -> GenreDto.builder()
                        .id(genre.getId())
                        .name(genre.getName())
                        .build()).toList() : null)
                .build();
    }

    public static Film mapDtoToFilm(FilmDto filmDto) {
        return Film.builder()
                .id(filmDto.getId())
                .name(filmDto.getName())
                .description(filmDto.getDescription())
                .releaseDate(filmDto.getReleaseDate() != null ? LocalDate.parse(filmDto.getReleaseDate()) : null)
                .duration(filmDto.getDuration())
                .mpa(filmDto.getMpa() != null ? MPA.builder()
                        .id(filmDto.getMpa().getId())
                        .name(filmDto.getMpa().getName())
                        .build() : null
                )
                .genres(filmDto.getGenres() != null ? filmDto.getGenres().stream().map(genreDto -> Genre.builder()
                        .id(genreDto.getId())
                        .name(genreDto.getName())
                        .build()).toList() : null)
                .build();
    }
}
