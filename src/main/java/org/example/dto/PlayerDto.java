package org.example.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {
    private Integer id;
    private String name;
    private String nameDeck;
    private Integer deckId;

    public PlayerDto(Integer id, String name, String nameDeck) {
        this.id = id;
        this.name = name;
        this.nameDeck = nameDeck;
    }

    public PlayerDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
