package hipravin.samples.jpaid.persist.concurrency;

import jakarta.persistence.*;

@Entity
@Table(name = "ACCOUNT")
@NamedQueries({
        @NamedQuery(name = "AccountEntity.findByIdsIn",
                query="select a from AccountEntity a where a.id in (:ids)")
})
public class AccountEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Basic
    @Column(name = "AMOUNT")
    private Long amount;

    public AccountEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
