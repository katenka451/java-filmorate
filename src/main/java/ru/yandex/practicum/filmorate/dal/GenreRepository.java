package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";
    private static final String FIND_BY_FILM_QUERY = "SELECT genre.*" +
            " FROM genre" +
            " INNER JOIN filmgenres ON filmgenres.genre_id = genre.id" +
            " AND filmgenres.film_id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM genre WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO genre (name) VALUES (?)";

    public GenreRepository(JdbcTemplate jdbc, GenreRowMapper mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public List<Genre> findByFilmId(Long filmId) {
        return findMany(FIND_BY_FILM_QUERY, filmId);
    }

    public Optional<Genre> getById(Long genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }

    public boolean delete(Long genreId) {
        return delete(DELETE_QUERY, genreId);
    }

    public Genre create(Genre genre) {
        long id = insert(
                INSERT_QUERY,
                genre.getName()
        );
        genre.setId(id);
        return genre;
    }
}
