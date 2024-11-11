package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendRepository {

    protected final JdbcTemplate jdbc;

    private static final String DELETE_QUERY = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?)";

    public boolean delete(Long userId, Long friendId) {
        return jdbc.update(DELETE_QUERY, userId, friendId) > 0;
    }

    public boolean create(Long userId, Long friendId) {
        return jdbc.update(INSERT_QUERY, userId, friendId) > 0;
    }

}
