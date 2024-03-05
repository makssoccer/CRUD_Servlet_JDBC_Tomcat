package org.example.mapper;

import org.example.dto.PlayerDto;
import org.example.entity.Player;

public interface PlayerConverter extends Converter<Player, PlayerDto> {
    PlayerDto entityToDtoWithDeck(Player player);
    Player dtoToEntityWithId(PlayerDto playerDto);
}
