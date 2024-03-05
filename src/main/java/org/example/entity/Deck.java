package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Deck extends BaseEntity {
    private String deckName;
    private List<Card> cards;
    private Player player;

    public Deck(Integer id, String deckName, List<Card> cards) {
        super(id);
        this.deckName = deckName;
        this.cards = cards;
    }

    public Deck(Integer id, String deckName) {
        super(id);
        this.deckName = deckName;
    }

    public Deck(Integer id) {
        super(id);
    }
}




