package org.example.mapper;

import org.example.dto.DeckDto;
import org.example.entity.Deck;

public interface DeckConverter extends Converter<Deck, DeckDto> {
    DeckDto dtoToEntityWithCard(Deck deck);
    Deck dtoToEntityWithId(DeckDto deckDto);

}
