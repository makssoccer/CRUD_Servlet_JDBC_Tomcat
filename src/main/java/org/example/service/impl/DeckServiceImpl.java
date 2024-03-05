package org.example.service.impl;

import org.example.dto.DeckDto;
import org.example.entity.Deck;
import org.example.mapper.impl.DeckConverterImpl;
import org.example.repository.DeckRepository;
import org.example.service.DeckService;

import java.util.List;
import java.util.stream.Collectors;

public class DeckServiceImpl implements DeckService {
    private final DeckRepository deckRepository;
    private final DeckConverterImpl deckConverter;

    public DeckServiceImpl(DeckRepository deckRepository, DeckConverterImpl deckConverter) {
        this.deckRepository = deckRepository;
        this.deckConverter = deckConverter;
    }

    @Override
    public DeckDto create(DeckDto deckDto) {
        Deck deck = deckConverter.dtoToEntity(deckDto);
        Deck savedDeck = deckRepository.save(deck).get();

        return deckConverter.entityToDto(savedDeck);
    }

    @Override
    public DeckDto read(Integer id) {

        Deck deck = deckRepository.getById(id)
                .orElseThrow(() -> new RuntimeException("Player not found."));

        return deckConverter.entityToDto(deck);
    }

    @Override
    public DeckDto update(DeckDto deckDto) {
        Deck deck = deckConverter.dtoToEntityWithId(deckDto);
        Deck updatedDeck = deckRepository.update(deck)
                .orElseThrow(() -> new RuntimeException("Deck not found."));

        return deckConverter.dtoToEntityWithCard(updatedDeck);
    }

    @Override
    public void delete(Integer id) {
        deckRepository.delete(id);
    }

    @Override
    public List<DeckDto> readAll() {
        return deckRepository.findAll().stream()
                .map(deck -> deckConverter.entityToDto(deck))
                .collect(Collectors.toList());
    }
}
