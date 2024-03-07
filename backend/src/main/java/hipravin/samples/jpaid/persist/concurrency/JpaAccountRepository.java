package hipravin.samples.jpaid.persist.concurrency;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class JpaAccountRepository implements AccountRepository {
    final EntityManager em;

    public JpaAccountRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void transfer(long accountIdFrom, long accountIdTo, long amount) {
        var query = em.createNamedQuery("AccountEntity.findByIdsIn", AccountEntity.class);
        query.setParameter("ids", List.of(accountIdFrom, accountIdTo));
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);//otherwise inconsistent data

        List<AccountEntity> accounts = query.getResultList();
        if(accounts.size() != 2) {
            throw new RuntimeException("Account pair not found: " + accountIdFrom + " / " + accountIdTo);
        }

        AccountEntity from = accounts.get(0).getId() == accountIdFrom ? accounts.get(0) : accounts.get(1);
        AccountEntity to = accounts.get(0).getId() == accountIdTo ? accounts.get(0) : accounts.get(1);

        from.setAmount(from.getAmount() - amount);
        to.setAmount(to.getAmount() + amount);
    }
}
