package CollectibleCardGame.service;

import org.example.dto.PlayerDto;
import org.example.entity.Deck;
import org.example.entity.Player;
import org.example.mapper.impl.PlayerConverterImpl;
import org.example.repository.PlayerRepository;
import org.example.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTest {
    @Mock
    private PlayerRepository playerRepository;
    @Spy
    private PlayerConverterImpl converter;
    @InjectMocks
    private PlayerServiceImpl playerService;

    private Player player = new Player(1, "player");
    private PlayerDto playerDto = new PlayerDto(1, "player");

    @Test
    void testCreate_returnPlayerDto() {
        when(playerRepository.save(player)).thenReturn(Optional.of(player));

        PlayerDto returnDto = playerService.create(playerDto);

        assertEquals(returnDto, playerDto);
    }

    @Test
    void read_shouldReturnDto_whenExist() {
        Integer id = 1;
        when(playerRepository.getById(id)).thenReturn(Optional.of(player));

        PlayerDto returnDto = playerService.read(id);

        assertEquals(returnDto, playerDto);
    }

    @Test
    void testRead_returnRuntimeException_whenNoExist() {
        assertThrows(RuntimeException.class, () -> playerService.read(2));
    }

    @Test
    void testUpdate_returnPlayerDto_whenExist() {
        PlayerDto updateDto = new PlayerDto(1, "update", "Kingdom");
        Player update = new Player(1, "update", new Deck());
        when(converter.dtoToEntityWithId(updateDto)).thenReturn(update);
        when(converter.entityToDtoWithDeck(update)).thenReturn(updateDto);
        when(playerRepository.update(update)).thenReturn(Optional.of(update));

        PlayerDto returnDto = playerService.update(updateDto);

        assertEquals(returnDto, updateDto);
    }

    @Test
    void testUpdate_returnRuntimeException_whenNoExist() {
        PlayerDto wrongDto = new PlayerDto(2, "wrong");

        assertThrows(RuntimeException.class, () -> playerService.update(wrongDto));
    }

    @Test
    void testDelete() {
        playerService.delete(1);

        verify(playerRepository, times(1)).delete(1);
    }
}