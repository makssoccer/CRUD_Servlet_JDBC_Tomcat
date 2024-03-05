package org.example.mapper.impl;

import org.example.dto.CardDto;
import org.example.entity.Card;
import org.example.mapper.Converter;
import org.modelmapper.ModelMapper;

public class CardConverterImpl implements Converter<Card, CardDto> {

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public CardDto entityToDto(Card card) {
        return mapper.map(card, CardDto.class);
    }

    @Override
    public Card dtoToEntity(CardDto cardDto) {
        return mapper.map(cardDto, Card.class);
    }

}
