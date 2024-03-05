package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeckDto {
    private Integer id;
    private String deckName;
    private List<String> cardsName;
    private List<Integer> cardId;
    private Integer playerId;

    public DeckDto(Integer id, String deckName, List<String> cardsName) {
        this.id = id;
        this.deckName = deckName;
        this.cardsName = cardsName;
    }

    public DeckDto(Integer id, String deckName) {
        this.id = id;
        this.deckName = deckName;
    }
}
