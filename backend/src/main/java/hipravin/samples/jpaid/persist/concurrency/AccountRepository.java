package hipravin.samples.jpaid.persist.concurrency;

public interface AccountRepository {
    void transfer(long accountIdFrom, long accountIdTo, long amount);
}
