package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepository {
    protected final JdbcTemplate jdbc;

    private static final String DELETE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";

    public boolean delete(Long filmId, Long userId) {
        return jdbc.update(DELETE_QUERY, filmId, userId) > 0;
    }

    public boolean create(Long filmId, Long userId) {
        return jdbc.update(INSERT_QUERY, filmId, userId) > 0;
    }
}
