package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreRepository.class, GenreRowMapper.class})
class GenreRepositoryTest {

    private final GenreRepository genreRepository;

    @Test
    void findAll() {
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(6);
    }

    @Test
    void findByFilmId() {
        List<Genre> genreForFilm = genreRepository.findByFilmId((long) 4);
        assertThat(genreForFilm).hasSize(2);
        genreRepository.getById((long) 1).ifPresent(genre_1 -> assertThat(genreForFilm.contains(genre_1)).isTrue());
        genreRepository.getById((long) 6).ifPresent(genre_6 -> assertThat(genreForFilm.contains(genre_6)).isTrue());
    }

    @Test
    void getById() {
        Optional<Genre> genreOptional = genreRepository.getById((long) 2);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", (long) 2)
                );
    }

    @Test
    void delete() {
        assertThat(genreRepository.delete((long) 2)).isTrue();
    }

    @Test
    void create() {
        Genre newGenre = Genre.builder()
                .name("Хоррор")
                .build();

        assertThat(genreRepository.getById(genreRepository.create(newGenre).getId()).isPresent()).isTrue();
    }
}