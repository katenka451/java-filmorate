package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.MPARowMapper;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

@Repository
public class MPARepository extends BaseRepository<MPA> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM MPA";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM MPA WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM MPA WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO MPA (name) VALUES (?)";

    public MPARepository(JdbcTemplate jdbc, MPARowMapper mapper) {
        super(jdbc, mapper);
    }

    public List<MPA> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<MPA> getById(Long mpaId) {
        return findOne(FIND_BY_ID_QUERY, mpaId);
    }

    public boolean delete(Long mpaId) {
        return delete(DELETE_QUERY, mpaId);
    }

    public MPA create(MPA mpa) {
        long id = insert(
                INSERT_QUERY,
                mpa.getName()
        );
        mpa.setId(id);
        return mpa;
    }
}
