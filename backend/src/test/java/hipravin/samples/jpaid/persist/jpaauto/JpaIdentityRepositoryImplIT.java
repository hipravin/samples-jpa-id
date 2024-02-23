package hipravin.samples.jpaid.persist.jpaauto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JpaIdentityRepositoryImplIT {
    @Autowired
    JpaIdentityRepositoryImpl jpaIdentityItemRepositoty;

    @BeforeEach
    void truncate() {
        jpaIdentityItemRepositoty.deleteAll();
    }
    @Test
    void testReserveItemsIds() {
        int count = 1_000_000;
        long start = System.nanoTime();
        List<Long> ids = jpaIdentityItemRepositoty.reserveItemIds(count);

        System.out.println("Reserve ids took: " + Duration.ofNanos(System.nanoTime() - start).toMillis() + " ms");

        assertEquals(count, ids.size());
        Set<Long> idsSet = new HashSet<>(ids);
        assertEquals(count, idsSet.size());
    }

    @Test
    void testBatchInsert() {
        String descriptionStub = "stub description for id ";
        String nameStub = "name";
//        int count = 1_048_576;
        int count = 1024;
        List<IdentityItemEntity> items = LongStream.rangeClosed(1, count)
                .mapToObj(id -> new IdentityItemEntity(null, nameStub + id,descriptionStub + id))
                .toList();

        long start = System.nanoTime();
        jpaIdentityItemRepositoty.persist(items);

        System.out.println("Insert took: " + Duration.ofNanos(System.nanoTime() - start).toMillis() + " ms");
    }

}