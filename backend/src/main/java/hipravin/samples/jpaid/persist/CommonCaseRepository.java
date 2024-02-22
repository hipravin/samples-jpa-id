package hipravin.samples.jpaid.persist;

import java.util.List;
import java.util.Optional;

public interface CommonCaseRepository<T, ID> {
    Optional<T> findById(ID id);

    /**
     * Persist a new entity.
     */
    <S extends T> S persist(S entity);

    /**
     * Persist all provided entities in single batch. Entities should be new.
     */
    <S extends T> Iterable<S> persist(List<S> entities);

    /**
     * Update existing entity.
     */
    <S extends T> S update(S entity);

    /**
     * Update all provided entities in single batch. Entities should exist in database.
     */
    <S extends T> Iterable<S> update(List<S> entities);

    void deleteAll();
}
