package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class, UserRowMapper.class})
class UserRepositoryTest {

    private final UserRepository userRepository;

    @Test
    void findAll() {
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(6);
    }

    @Test
    void getById() {
        Optional<User> userOptional = userRepository.getById((long) 1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", (long) 1)
                );
    }

    @Test
    void findAllFriends() {
        Optional<User> userOptional = userRepository.getById((long) 1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", (long) 1)
                );

        List<User> friends = userRepository.findAllFriends((long) 1);
        assertThat(friends).hasSize(4);
    }

    @Test
    void findCommonFriends() {
        Optional<User> userOptional1 = userRepository.getById((long) 1);
        assertThat(userOptional1)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", (long) 1)
                );

        Optional<User> userOptional2 = userRepository.getById((long) 2);
        assertThat(userOptional2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", (long) 2)
                );

        List<User> commonFriends = userRepository.findCommonFriends((long) 1, (long) 2);
        assertThat(commonFriends).hasSize(2);

        userRepository.getById((long) 3).ifPresent(user3 -> assertThat(commonFriends.contains(user3)).isTrue());
        userRepository.getById((long) 4).ifPresent(user4 -> assertThat(commonFriends.contains(user4)).isTrue());
    }

    @Test
    void delete() {
        assertThat(userRepository.delete((long) 6)).isTrue();
    }

    @Test
    void create() {
        User newUser = User.builder()
                .email("user6@gmail.com")
                .login("user_6")
                .name("User 6")
                .birthday(LocalDate.parse("1987-11-05"))
                .build();

        assertThat(userRepository.getById(userRepository.create(newUser).getId()).isPresent()).isTrue();
    }

    @Test
    void update() {
        User updUser = userRepository.getById((long) 1).orElse(null);
        assertThat(updUser).isNotNull();
        updUser.setEmail("user1@gmail.com");
        updUser = userRepository.update(updUser);
        updUser = userRepository.getById((long) 1).orElse(null);
        assertThat(updUser).isNotNull();
        assertThat(updUser.getEmail()).isEqualTo("user1@gmail.com");
    }
}