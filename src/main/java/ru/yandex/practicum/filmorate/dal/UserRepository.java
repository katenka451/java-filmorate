package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String IS_EMAIL_USED_QUERY = "SELECT id FROM users WHERE email = ? LIMIT 1";
    private static final String FIND_ALL_FRIENDS =
            "SELECT u.*" +
                    " FROM friendship AS f" +
                    " INNER JOIN users AS u on u.id = f.friend_id" +
                    " WHERE f.user_id = ?";
    private static final String FIND_COMMON_FRIENDS =
            "SELECT users.*" +
                    " FROM friendship AS f1" +
                    " INNER JOIN friendship AS f2 ON f2.user_id = ?" +
                    " AND f2.friend_id = f1.friend_id" +
                    " INNER JOIN users on users.id = f2.friend_id" +
                    " WHERE f1.user_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday)" +
            " VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?," +
            " birthday = ? WHERE id = ?";

    public UserRepository(JdbcTemplate jdbc, UserRowMapper mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<User> getById(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public boolean isEmailUsed(String email) {
        return isExist(IS_EMAIL_USED_QUERY, email);
    }

    public List<User> findAllFriends(Long userId) {
        return findMany(FIND_ALL_FRIENDS, userId);
    }

    public List<User> findCommonFriends(Long userId1, Long userId2) {
        return findMany(FIND_COMMON_FRIENDS, userId1, userId2);
    }

    public boolean delete(Long userId) {
        return delete(DELETE_QUERY, userId);
    }

    public User create(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }
}
