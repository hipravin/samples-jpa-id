package hipravin.samples.jpaid.persist.sqlinjection;

import java.util.List;

public interface PersonRepository {
    List<PersonEntity> findByName(String name);
}
