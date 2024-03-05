package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"},callSuper = true)
public class Card extends BaseEntity {
    private String cardName;
    private Integer power;
    private Deck deck;

    public Card (Integer id, String cardName, Integer power){
        super(id);
        this.cardName = cardName;
        this.power = power;
    }

    public Card(String cardName, Integer power) {
        this.cardName = cardName;
        this.power = power;
    }

    public Card(Integer id) {
        super(id);
    }
}
