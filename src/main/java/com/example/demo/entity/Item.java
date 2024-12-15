package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;


@Entity
@Getter
@DynamicInsert
// TODO: 6. Dynamic Insert
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;


//    @Column(name = "item_status", nullable = false, columnDefinition = "varchar(20) default 'PENDING'")
    @Column(name = "item_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ItemStatus itemStatus;

    public Item(String name, String description, User manager, User owner) {
        this.name = name;
        this.description = description;
        this.manager = manager;
        this.owner = owner;
    }

    @Builder
    public Item(String name, String description, User manager, User owner, ItemStatus itemStatus) {
        this.name = name;
        this.description = description;
        this.manager = manager;
        this.owner = owner;
        this.itemStatus = itemStatus;
    }

    public Item() {}

    public void setId(long l) {
    }
}
