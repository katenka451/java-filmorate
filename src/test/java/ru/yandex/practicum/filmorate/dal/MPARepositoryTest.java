package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.MPARowMapper;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MPARepository.class, MPARowMapper.class})
class MPARepositoryTest {

    private final MPARepository mpaRepository;

    @Test
    void findAll() {
        List<MPA> ratings = mpaRepository.findAll();
        assertThat(ratings).hasSize(5);
    }

    @Test
    void getById() {
        Optional<MPA> mpaOptional = mpaRepository.getById((long) 1);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", (long) 1)
                );
    }

    @Test
    void delete() {
        assertThat(mpaRepository.delete((long) 2)).isTrue();
    }

    @Test
    void create() {
        MPA newMPA = MPA.builder()
                .name("new_mpa")
                .build();

        assertThat(mpaRepository.getById(mpaRepository.create(newMPA).getId()).isPresent()).isTrue();
    }
}