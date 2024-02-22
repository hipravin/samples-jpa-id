package hipravin.samples.jpaid.persist.jdbc;

import hipravin.samples.jpaid.persist.CommonCaseRepository;
import hipravin.samples.jpaid.persist.Item;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JdbcItemRepositoryImpl implements CommonCaseRepository<Item, Long> {
    private final JdbcTemplate jdbcTemplate;

    private static final int BATCH_SIZE = 128;
//    private static final int BATCH_SIZE = 64;

    private static final String TRUNCATE_ITEM_QUERY = "TRUNCATE ITEM";

    private static final String INSERT_BATCH_QUERY = """
            INSERT INTO ITEM (ID, NAME, DESCRIPTION)
               VALUES (?, ? ,?)""";

    public JdbcItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Item> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public <S extends Item> S persist(S entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Item> Iterable<S> persist(List<S> items) {
        int[][] result = jdbcTemplate.batchUpdate(INSERT_BATCH_QUERY,
                items,
                BATCH_SIZE,
                (ps, item) -> {
                    ps.setLong(1, item.id());
                    ps.setString(2, item.name());
                    ps.setString(3, item.description());
                });

        return items;
    }

    @Override
    public <S extends Item> S update(S entity) {
        return null;
    }

    @Override
    public <S extends Item> Iterable<S> update(List<S> entities) {
        return null;
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(TRUNCATE_ITEM_QUERY);
    }
}
