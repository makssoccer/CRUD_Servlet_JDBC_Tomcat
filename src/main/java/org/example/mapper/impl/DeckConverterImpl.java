package org.example.mapper.impl;

import org.example.dto.DeckDto;
import org.example.entity.Card;
import org.example.entity.Deck;
import org.example.mapper.DeckConverter;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class DeckConverterImpl implements DeckConverter {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public DeckDto entityToDto(Deck deck) {
        return mapper.map(deck, DeckDto.class);
    }

    @Override
    public DeckDto dtoToEntityWithCard(Deck deck) {
        List<String> cards = deck.getCards().stream()
                .map(cardId -> cardId.getCardName())
                .collect(Collectors.toList());

        return new DeckDto(deck.getId(), deck.getDeckName(), cards);
    }

    @Override
    public Deck dtoToEntity(DeckDto deckDto) {
        return mapper.map(deckDto, Deck.class);
    }

    @Override
    public Deck dtoToEntityWithId(DeckDto deckDto) {
        List<Card> cards = deckDto.getCardId().stream()
                .map(cardId -> new Card(cardId))
                .collect(Collectors.toList());

        return new Deck(deckDto.getId(), deckDto.getDeckName(), cards);
    }
}
