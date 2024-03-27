package hipravin.samples.jpaid.persist.sqlinjection;

import jakarta.persistence.*;

@Entity
@Table(name = "PERSON")
public class PersonEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Basic
    @Column(name = "AMOUNT")
    private String name;

    public PersonEntity() {
    }

    public PersonEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
