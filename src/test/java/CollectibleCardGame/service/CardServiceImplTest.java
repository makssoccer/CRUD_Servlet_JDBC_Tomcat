package CollectibleCardGame.service;

import org.example.dto.CardDto;
import org.example.entity.Card;
import org.example.mapper.impl.CardConverterImpl;
import org.example.repository.CardRepository;
import org.example.service.impl.CardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)

class CardServiceImplTest {
    @Mock
    private CardRepository cardRepository;
    @Spy
    private CardConverterImpl converter;
    @InjectMocks
    private CardServiceImpl cardService;

    private Card card = new Card(1, "test_card", 5);
    private CardDto cardDto = new CardDto(1, "test_card",5);

    @Test
    void testCreate_returnCardDto() {
        when(cardRepository.save(card)).thenReturn(Optional.of(card));

        CardDto returnDto = cardService.create(cardDto);
        assertEquals(returnDto, cardDto);
    }

    @Test
    void testRead_returnCardDto_whenExist() {
        Integer id = 1;
        when(cardRepository.getById(id)).thenReturn(Optional.of(card));

        CardDto returnDto = cardService.read(id);

        assertEquals(returnDto, cardDto);
    }

    @Test
    void testRead_returnRuntimeException_whenNoExist() {

        assertThrows(RuntimeException.class, () -> cardService.read(2));
    }

    @Test
    void testUpdate_returnCardDto_whenExist() {
        CardDto updateDto = new CardDto(1, "update_card", 1);
        Card update = new Card(1, "update_card", 1);
        when(cardRepository.update(update)).thenReturn(Optional.of(update));

        CardDto returnDto = cardService.update(updateDto);

        assertEquals(returnDto, updateDto);
    }

    @Test
    void testUpdate_returnRuntimeException_whenNoExist() {
        CardDto wrongDto = new CardDto(1, "wrong_card", 5);

        assertThrows(RuntimeException.class, () -> cardService.update(wrongDto));
    }

    @Test
    void testDelete() {
        cardService.delete(1);

        verify(cardRepository, times(1)).delete(1);
    }
}