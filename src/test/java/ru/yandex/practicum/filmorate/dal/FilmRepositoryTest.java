package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepository.class, FilmRowMapper.class})
class FilmRepositoryTest {

    private final FilmRepository filmRepository;

    @Test
    void findAll() {
        List<Film> films = filmRepository.findAll();
        assertThat(films).hasSize(5);
    }

    @Test
    void getById() {
        Optional<Film> filmOptional = filmRepository.getById((long) 1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", (long) 1)
                );
    }

    @Test
    void getBestFilms() {
        List<Film> bestFilms = filmRepository.getBestFilms((long) 2);
        assertThat(bestFilms).hasSize(2);
        filmRepository.getById((long) 1).ifPresent(film1 -> assertThat(bestFilms.contains(film1)).isTrue());
        filmRepository.getById((long) 3).ifPresent(film3 -> assertThat(bestFilms.contains(film3)).isTrue());
    }

    @Test
    void delete() {
        assertThat(filmRepository.delete((long) 5)).isTrue();
    }

    @Test
    void create() {
        Film newFilm = Film.builder()
                .name("Film 6")
                .description("Film 6")
                .releaseDate(LocalDate.parse("1987-11-05"))
                .duration(75)
                .build();

        assertThat(filmRepository.getById(filmRepository.create(newFilm).getId()).isPresent()).isTrue();
    }

    @Test
    void update() {
        Film updFilm = filmRepository.getById((long) 1).orElse(null);
        assertThat(updFilm).isNotNull();
        updFilm.setDuration(72);
        updFilm = filmRepository.update(updFilm);
        updFilm = filmRepository.getById((long) 1).orElse(null);
        assertThat(updFilm).isNotNull();
        assertThat(updFilm.getDuration()).isEqualTo(72);
    }
}