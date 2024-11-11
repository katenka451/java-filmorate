package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String FIND_BEST_FILMS
            = "SELECT *, (SELECT COUNT(1) FROM likes" +
            " WHERE film_id = f.id) AS cnt " +
            " FROM films AS f " +
            " ORDER BY cnt DESC" +
            " LIMIT ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films (name, description, release_date, duration, mpa_id)" +
            " VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_GENRES = "INSERT INTO filmgenres (film_id, genre_id) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
            " duration = ?, mpa_id = ? WHERE id = ?";
    private static final String UPDATE_GENRES = "INSERT INTO filmgenres (film_id, genre_id) VALUES (?, ?)" +
            " ON CONFLICT (film_id, genre_id) DO NOTHING";

    public FilmRepository(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Film> getById(Long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    public List<Film> getBestFilms(Long count) {
        return findMany(FIND_BEST_FILMS, count);
    }

    public boolean delete(Long filmId) {
        return delete(DELETE_QUERY, filmId);
    }

    public Film create(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null
        );
        film.setId(id);

        if (film.getGenres() != null) {
            for (Genre g : film.getGenres()) {
                insertWithComplexPK(INSERT_GENRES, film.getId(), g.getId());
            }
        }

        return film;
    }

    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId()
        );

        if (film.getGenres() != null) {
            for (Genre g : film.getGenres()) {
                insertWithComplexPK(UPDATE_GENRES, film.getId(), g.getId());
            }
        }

        return film;
    }

}
