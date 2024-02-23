package hipravin.samples.jpaid.persist.jdbc;

import com.google.common.collect.Iterables;
import hipravin.samples.jpaid.persist.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@SpringBootTest
class JdbcItemRepositotyImplIT {
    @Autowired
    JdbcItemRepositoryImpl jdbcItemRepositoty;

    @BeforeEach
    void truncate() {
        jdbcItemRepositoty.deleteAll();
    }

    @Test
    void testBatchInsert() {
        String descriptionStub = "stub description for id ";
        String nameStub = "name";
        List<Item> items = LongStream.rangeClosed(1, 1_048_576)
                .mapToObj(id -> new Item(id, nameStub + id,descriptionStub + id))
                .toList();

        long start = System.nanoTime();
        jdbcItemRepositoty.persist(items);

        System.out.println("Insert took: " + Duration.ofNanos(System.nanoTime() - start).toMillis() + " ms");
    }

    static <T> Iterable<List<T>> partitionUsingGuava(Stream<T> source, int batchSize) {
        return Iterables.partition(source::iterator, batchSize);
    }
}