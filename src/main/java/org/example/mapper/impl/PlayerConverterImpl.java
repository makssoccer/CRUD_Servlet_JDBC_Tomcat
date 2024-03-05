package org.example.mapper.impl;

import org.example.dto.PlayerDto;
import org.example.entity.Deck;
import org.example.entity.Player;
import org.example.mapper.PlayerConverter;
import org.modelmapper.ModelMapper;

import java.util.function.Function;

public class PlayerConverterImpl implements PlayerConverter {
    private final ModelMapper mapper = new ModelMapper();

    //checking if the player has a deck
    Function<Player, String> lambda = player -> player.getDeck() != null ? player.getDeck().getDeckName() : "Doesn't have";

    @Override
    public PlayerDto entityToDto(Player player) {
        return mapper.map(player, PlayerDto.class);
    }

    @Override
    public PlayerDto entityToDtoWithDeck(Player player) {
        return new PlayerDto(player.getId(), player.getName(), lambda.apply(player));
    }

    @Override
    public Player dtoToEntity(PlayerDto player) {
        return mapper.map(player, Player.class);
    }

    @Override
    public Player dtoToEntityWithId(PlayerDto playerDto) {
        return new Player(playerDto.getId(), playerDto.getName(), new Deck(playerDto.getDeckId()));
    }

}
