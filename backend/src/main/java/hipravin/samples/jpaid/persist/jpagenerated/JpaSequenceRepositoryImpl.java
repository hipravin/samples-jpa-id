package hipravin.samples.jpaid.persist.jpagenerated;

import hipravin.samples.jpaid.persist.CommonCaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JpaSequenceRepositoryImpl implements CommonCaseRepository<SequenceItemEntity, Long> {
    private final EntityManager entityManager;
    private static final String ITEM_ID_SEQ = "ITEM_ID_SEQ";
    private static final String ITEM_ID_FETCH_SEQ_QUERY = """
        SELECT NEXTVAL('ITEM_ID_SEQ') AS ID
            FROM GENERATE_SERIES(1, :count)""";

    public JpaSequenceRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    List<Long> reserveItemIds(int count) {
        var reserveIdsQuery = entityManager.createNativeQuery(ITEM_ID_FETCH_SEQ_QUERY, Long.class);
        reserveIdsQuery.setParameter("count", count);

        return (List<Long>)reserveIdsQuery.getResultList();
    }

    @Override
    public Optional<SequenceItemEntity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public <S extends SequenceItemEntity> S persist(S item) {
        entityManager.persist(item);
        return item;
    }

    @Override
    public <S extends SequenceItemEntity> Iterable<S> persist(List<S> items) {
        for (SequenceItemEntity item : items) {
            entityManager.persist(item);
        }
        return items;
    }

    @Override
    public <S extends SequenceItemEntity> S update(S entity) {
        return null;
    }

    @Override
    public <S extends SequenceItemEntity> Iterable<S> update(List<S> entities) {
        return null;
    }

    @Override
    public void deleteAll() {
        Query truncate = entityManager.createNativeQuery("TRUNCATE TABLE ITEM");
        truncate.executeUpdate();
    }
}
