package org.example.service.impl;

import org.example.dto.PlayerDto;
import org.example.entity.Player;
import org.example.mapper.impl.PlayerConverterImpl;
import org.example.repository.PlayerRepository;
import org.example.service.PlayerService;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerConverterImpl playerConverter;

    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerConverterImpl playerConverter) {
        this.playerRepository = playerRepository;
        this.playerConverter = playerConverter;
    }

    @Override
    public PlayerDto create(PlayerDto playerDto) {
        Player player = playerConverter.dtoToEntity(playerDto);
        Player savedPlayer = playerRepository.save(player).get();
        return playerConverter.entityToDto(savedPlayer);
    }

    @Override
    public PlayerDto read(Integer id) {
        Player deck = playerRepository.getById(id)
                .orElseThrow(() -> new RuntimeException("Player not found."));

        return playerConverter.entityToDto(deck);
    }

    @Override
    public PlayerDto update(PlayerDto playerDto) {
        Player player = playerConverter.dtoToEntityWithId(playerDto);
        Player updatedPlayer = playerRepository.update(player)
                .orElseThrow(() -> new RuntimeException("Player not found."));

        return playerConverter.entityToDtoWithDeck(updatedPlayer);
    }

    @Override
    public void delete(Integer id) {
        playerRepository.delete(id);
    }

    @Override
    public List<PlayerDto> readAll() {
        return playerRepository.findAll().stream()
                .map(player -> playerConverter.entityToDtoWithDeck(player))
                .collect(Collectors.toList());
    }
}
