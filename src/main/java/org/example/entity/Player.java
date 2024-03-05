package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Player extends BaseEntity {
    private String name;
    private Deck deck;

    public Player(Integer id, String name, Deck deck) {
        super(id);
        this.name = name;
        this.deck = deck;
    }

    public Player(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public Player(Integer id) {
        super(id);
    }
}
