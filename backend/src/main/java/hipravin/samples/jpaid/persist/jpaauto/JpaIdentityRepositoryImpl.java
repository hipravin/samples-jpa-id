package hipravin.samples.jpaid.persist.jpaauto;

import hipravin.samples.jpaid.persist.CommonCaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JpaIdentityRepositoryImpl implements CommonCaseRepository<IdentityItemEntity, Long> {
    private final EntityManager entityManager;
    private static final String ITEM_ID_SEQ = "ITEM_ID_SEQ";
    private static final String ITEM_ID_FETCH_SEQ_QUERY = """
        SELECT NEXTVAL('ITEM_ID_SEQ') AS ID
            FROM GENERATE_SERIES(1, :count)""";

    public JpaIdentityRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    List<Long> reserveItemIds(int count) {
        var reserveIdsQuery = entityManager.createNativeQuery(ITEM_ID_FETCH_SEQ_QUERY, Long.class);
        reserveIdsQuery.setParameter("count", count);

        return (List<Long>)reserveIdsQuery.getResultList();
    }

    @Override
    public Optional<IdentityItemEntity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public <S extends IdentityItemEntity> S persist(S item) {
        entityManager.persist(item);
        return item;
    }

//    @Override
//    public <S extends IdentityItemEntity> Iterable<S> persist(List<S> entities) {
//        return null;
//    }

    @Override
    public <S extends IdentityItemEntity> Iterable<S> persist(List<S> items) {
        for (IdentityItemEntity item : items) {
            entityManager.persist(item);
        }
        return items;
    }

    @Override
    public <S extends IdentityItemEntity> S update(S entity) {
        return null;
    }

    @Override
    public <S extends IdentityItemEntity> Iterable<S> update(List<S> entities) {
        return null;
    }

    @Override
    public void deleteAll() {
        Query truncate = entityManager.createNativeQuery("TRUNCATE TABLE ITEM");
        truncate.executeUpdate();
    }
}
