package CollectibleCardGame.service;

import org.example.dto.DeckDto;
import org.example.entity.Card;
import org.example.entity.Deck;
import org.example.mapper.impl.DeckConverterImpl;
import org.example.repository.DeckRepository;
import org.example.service.impl.DeckServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)

class DeckServiceImplTest {
    @Mock
    private DeckRepository deckRepository;
    @Spy
    private DeckConverterImpl converter;
    @InjectMocks
    private DeckServiceImpl deckService;

    private Deck deck = new Deck(1, "deck_test");
    private DeckDto deckDto = new DeckDto(1, "deck_test");

    @Test
    void testCreate_returnDeckDto() {
        when(deckRepository.save(deck)).thenReturn(Optional.of(deck));

        DeckDto returnDto = deckService.create(deckDto);

        assertEquals(returnDto, deckDto);
    }

    @Test
    void testRead_returnDeckDto_whenExist() {
        Integer id = 1;
        when(deckRepository.getById(id)).thenReturn(Optional.of(deck));

        DeckDto returnDto = deckService.read(id);

        assertEquals(returnDto, deckDto);
    }

    @Test
    void testRead_returnRuntimeException_whenNoExist() {

        assertThrows(RuntimeException.class, () -> deckService.read(2));
    }

    @Test
    void testUpdate_returnDeckDto_whenExist() {
        //Создаю карту для проверки конвертации Id карт в название возвращаемых карт
        Card card = new Card(1, "Queen", 9);
        List<Card> cardList = new ArrayList<>();
        cardList.add(card);
        //Лист Id карта которые будут добавлены к сущности Deck
        List<Integer> cardId = new ArrayList<>();
        cardId.add(card.getId());

        DeckDto QueryUpdateDto = new DeckDto(1, "update",null, cardId,null);//конвертированная Dto c JSON
        Deck update = new Deck(1, "update", cardList);
        when(deckRepository.update(update)).thenReturn(Optional.of(update));


        DeckDto returnDto = deckService.update(QueryUpdateDto);

        List<String> cardName = new ArrayList<>();
        cardName.add(card.getCardName());
        DeckDto returnUpdateDto = new DeckDto(1, "update",cardName);//Пришедшая Dto после update


        assertEquals(returnDto, returnUpdateDto);
    }

    @Test
    void testUpdate_returnRuntimeException_whenNoExist() {
        DeckDto wrongDto = new DeckDto(2, "wrong");

        assertThrows(RuntimeException.class, () -> deckService.update(wrongDto));
    }

    @Test
    void testDelete() {
        deckService.delete(1);

        verify(deckRepository, times(1)).delete(1);
    }

}