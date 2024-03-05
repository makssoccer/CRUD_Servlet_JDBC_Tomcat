package org.example.service.impl;

import org.example.dto.CardDto;
import org.example.entity.Card;
import org.example.mapper.impl.CardConverterImpl;
import org.example.repository.CardRepository;
import org.example.service.CardService;

import java.util.List;
import java.util.stream.Collectors;

public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardConverterImpl cardConverter;

    public CardServiceImpl(CardRepository cardRepository, CardConverterImpl cardConverter) {
        this.cardRepository = cardRepository;
        this.cardConverter = cardConverter;
    }

    @Override
    public CardDto create(CardDto cardDto) {
        Card card = cardConverter.dtoToEntity(cardDto);
        Card savedCard = cardRepository.save(card).get();

        return cardConverter.entityToDto(savedCard);
    }

    @Override
    public CardDto read(Integer id) {
        Card card = cardRepository.getById(id)
                .orElseThrow(() -> new RuntimeException("Card not found."));

        return cardConverter.entityToDto(card);
    }

    @Override
    public CardDto update(CardDto cardDto) {
        Card card = cardConverter.dtoToEntity(cardDto);
        Card updatedCard = cardRepository.update(card)
                .orElseThrow(() -> new RuntimeException("Card not found."));

        return cardConverter.entityToDto(updatedCard);
    }

    @Override
    public void delete(Integer id) {
        cardRepository.delete(id);
    }

    @Override
    public List<CardDto> readAll() {
        return cardRepository.findAll().stream()
                .map(card -> cardConverter.entityToDto(card))
                .collect(Collectors.toList());
    }
}
