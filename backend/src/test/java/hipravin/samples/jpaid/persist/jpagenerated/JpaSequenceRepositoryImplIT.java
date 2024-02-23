package hipravin.samples.jpaid.persist.jpagenerated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;
import java.util.stream.LongStream;

@SpringBootTest
class JpaSequenceRepositoryImplIT {
    @Autowired
    JpaSequenceRepositoryImpl jpaSequenceItemRepositoty;

    @BeforeEach
    void truncate() {
        jpaSequenceItemRepositoty.deleteAll();
    }

    @Test
    void testInsert() {
        String descriptionStub = "stub description for id ";
        String nameStub = "name";
//        int count = 1_048_576;
        int count = 1000;
        List<SequenceItemEntity> items = LongStream.rangeClosed(1, count)
                .mapToObj(id -> new SequenceItemEntity(null, nameStub + id,descriptionStub + id))
                .toList();

        long start = System.nanoTime();
        items.forEach(item -> {
            jpaSequenceItemRepositoty.persist(item);
        });

        System.out.println("Insert took: " + Duration.ofNanos(System.nanoTime() - start).toMillis() + " ms");
    }

    @Test
    void testBatchInsert() {
        String descriptionStub = "stub description for id ";
        String nameStub = "name";
        int count = 1_048_576;
//        int count = 1024;
        List<SequenceItemEntity> items = LongStream.rangeClosed(1, count)
                .mapToObj(id -> new SequenceItemEntity(null, nameStub + id,descriptionStub + id))
                .toList();

        long start = System.nanoTime();
        jpaSequenceItemRepositoty.persist(items);

        System.out.println("Insert took: " + Duration.ofNanos(System.nanoTime() - start).toMillis() + " ms");
    }

}