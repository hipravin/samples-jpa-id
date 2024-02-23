package hipravin.samples.jpaid.persist.jpagenerated;

import jakarta.persistence.*;

@Entity
@Table(name = "ITEM")
public class SequenceItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemIdSeq")
    @SequenceGenerator(sequenceName = "ITEM_ID_SEQ", allocationSize = 100, name = "itemIdSeq")
    @Column(name = "ID")
    private Long id;

    @Basic
    @Column(name = "NAME")
    private String name;

    @Basic
    @Column(name = "DESCRIPTION")
    private String description;

    public SequenceItemEntity() {
    }

    public SequenceItemEntity(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
