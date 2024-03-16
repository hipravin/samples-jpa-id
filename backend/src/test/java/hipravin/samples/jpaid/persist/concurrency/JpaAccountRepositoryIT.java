package hipravin.samples.jpaid.persist.concurrency;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
class JpaAccountRepositoryIT {

    @Autowired
    JpaAccountRepository jpaAccountRepository;
    @Autowired
    JdbcAccountRepository jdbcAccountRepository;

    @Test
    void transferJdbc() {
        jdbcAccountRepository.transfer(1,2,17);
    }

    @Test
    void someTransfer() {
        jpaAccountRepository.transfer(1,2, 17);
    }


    @Test
    void bulkTransfer() {
        CountDownLatch startLatch = new CountDownLatch(1);
        AccountRepository repo = jpaAccountRepository;
//        AccountRepository repo = jdbcAccountRepository;
        Runnable transfer1 = () -> {
            repo.transfer(1, 2, 1);
        };
        Runnable transfer2 = () -> {
            repo.transfer(2, 3, 1);
        };
        Runnable transfer3 = () -> {
            repo.transfer(3, 1, 1);
//            repo.transfer(1, 3, -1);
        };

        int iterations = 1000;
        long start = System.nanoTime();
        var cf1 = iterateAsync(startLatch, iterations, transfer1);
        var cf2 = iterateAsync(startLatch, iterations, transfer2);
        var cf3 = iterateAsync(startLatch, iterations, transfer3);

        startLatch.countDown();
        CompletableFuture.allOf(cf1, cf2, cf3).join();
        System.out.println("Queries took: " + Duration.ofNanos(System.nanoTime() - start).toMillis() + " ms.");
    }

    CompletableFuture<Void> iterateAsync(CountDownLatch startLatch, int iterations, Runnable task) {
        return CompletableFuture.runAsync(() -> {
            try {
                startLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            for (int i = 0; i < iterations; i++) {
                 task.run();
            }
        });
    }

}