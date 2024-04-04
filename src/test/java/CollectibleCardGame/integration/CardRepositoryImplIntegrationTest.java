package CollectibleCardGame.integration;

import org.example.configuration.DataSourceCofig;
import org.example.entity.Card;
import org.example.repository.impl.CardRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CardRepositoryImplIntegrationTest {

    private CardRepositoryImpl cardRepository;
    private DataSource dataSource;
    private Connection connection;
    private final String INSERT = "INSERT INTO card (card_name, power) VALUES (?, ?)";
    private final String CREATE = "CREATE TABLE IF NOT EXISTS card (id SERIAL PRIMARY KEY, card_name VARCHAR(255), power INT)";
    private final String DROP = "DROP TABLE IF EXISTS card";

    @BeforeEach
    void setUp() throws SQLException {
        dataSource = DataSourceCofig.getDataSource();
        cardRepository = new CardRepositoryImpl(dataSource);
        connection = DataSourceCofig.getConnection();
        dropTestTable();
        createTestTable();
    }

    @AfterEach
    void tearDown() throws SQLException {

        connection.close();
    }

    @Test
    void save_ShouldSaveCard_WhenValidCardPassed() {
        // Arrange
        Card card = new Card("Test Card", 10);

        // Act
        Optional<Card> savedCard = cardRepository.save(card);

        // Assert
        assertTrue(savedCard.isPresent());
        assertEquals(card.getCardName(), savedCard.get().getCardName());
        assertEquals(card.getPower(), savedCard.get().getPower());
        assertNotNull(savedCard.get().getId());
    }

    @Test
    void findAll_ShouldReturnAllCards() throws SQLException {
        // Arrange
        insertTestCard("Test Card 1", 10);
        insertTestCard("Test Card 2", 20);
        insertTestCard("Test Card 3", 30);

        // Act
        List<Card> cards = cardRepository.findAll();

        // Assert
        assertEquals(3, cards.size());
    }

    @Test
    void getById_ShouldReturnDeck_WhenValidIdPassed() throws SQLException {
        // Arrange
        Card card = new Card(1, "Test Deck", 7);
        insertTestCard("Test Deck", 7);

        // Act
        Optional<Card> retrievedDeck = cardRepository.getById(card.getId());

        // Assert
        assertEquals(card.getCardName(), retrievedDeck.get().getCardName());
    }

    @Test
    void update_ShouldUpdateDeck_WhenValidDeckPassed() throws SQLException {
        // Arrange
        Card card = new Card(1, "Test Card", 8);
        insertTestCard("Test Card", 8);
        card.setCardName("Updated Deck");

        // Act
        Card updatedCard = cardRepository.update(card).get();

        // Assert
        assertEquals(card.getCardName(), updatedCard.getCardName());
    }

    @Test
    void delete_ShouldDeleteCard_WhenValidIdPassed() throws SQLException {
        // Arrange
        Card card = new Card(1, "Test Card", 10);
        insertTestCard("Test Card", 10);

        // Act
        cardRepository.delete(card.getId());
        Optional<Card> deletedCard = cardRepository.getById(card.getId());

        // Assert
        assertTrue(deletedCard.isEmpty());
    }


    private void createTestTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE);
        }
    }

    private void dropTestTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP);
        }
    }

    private void insertTestCard(String cardName, int power) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setString(1, cardName);
            preparedStatement.setInt(2, power);
            preparedStatement.executeUpdate();
        }
    }
}