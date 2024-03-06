package CollectibleCardGame.integration;

import org.example.configuration.DataSourceCofig;
import org.example.entity.Deck;
import org.example.repository.impl.DeckRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class DeckRepositoryImplIntegrationTest {

    private DeckRepositoryImpl deckRepository;
    private DataSource dataSource;
    private Connection connection;
    private final String INSERT = "INSERT INTO deck (deck_name) VALUES (?)";
    private final String CREATE = "CREATE TABLE IF NOT EXISTS deck (id SERIAL PRIMARY KEY, deck_name VARCHAR(255),player_id INT)";
    private final String DROP = "DROP TABLE IF EXISTS deck";

    @BeforeEach
    void setUp() throws SQLException {
        dataSource = DataSourceCofig.getDataSource();
        deckRepository = new DeckRepositoryImpl(dataSource);
        connection = dataSource.getConnection();
        dropTestTable();
        createTestTable();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void save_ShouldSaveDeck_WhenValidDeckPassed() {
        // Arrange
        Deck deck = new Deck(1,"Test Deck");

        // Act
        Optional<Deck> savedDeck = deckRepository.save(deck);

        // Assert
        assertAll(() -> assertTrue(savedDeck.isPresent()),
                () ->assertEquals(deck.getDeckName(), savedDeck.get().getDeckName()),
                () -> assertNotNull(savedDeck.get().getId()));
    }

    @Test
    void findAll_ShouldReturnAllDecks() throws SQLException {
        // Arrange
        insertTestDeck("Test Deck 1");
        insertTestDeck("Test Deck 2");
        insertTestDeck("Test Deck 3");

        // Act
        List<Deck> decks = deckRepository.findAll();

        // Assert
        assertEquals(3, decks.size());
    }

    @Test
    void getById_ShouldReturnDeck_WhenValidIdPassed() throws SQLException {
        // Arrange
        Deck deck = new Deck(1,"Test Deck");
        insertTestDeck("Test Deck");

        // Act
        Optional<Deck> retrievedDeck = deckRepository.getById(deck.getId());

        // Assert
        assertAll(() -> assertTrue(retrievedDeck.isPresent()),
                () -> assertEquals(deck.getDeckName(), retrievedDeck.get().getDeckName()));
    }

    @Test
    void update_ShouldUpdateDeck_WhenValidDeckPassed() throws SQLException {
        // Arrange
        Deck deck = new Deck(1,"Test Deck");
        insertTestDeck("Test Deck");
        deck.setDeckName("Updated Deck");

        // Act
        Optional<Deck> updatedDeck = deckRepository.update(deck);

        // Assert
        assertAll(() -> assertTrue(updatedDeck.isPresent()),
                () -> assertEquals(deck.getDeckName(), updatedDeck.get().getDeckName()));

    }

    @Test
    void delete_ShouldDeleteDeck_WhenValidIdPassed() throws SQLException {
        // Arrange
        Deck deck = new Deck(1,"Test Deck");
        insertTestDeck("Test Deck");

        // Act
        deckRepository.delete(deck.getId());
        Optional<Deck> deletedDeck = deckRepository.getById(deck.getId());

        // Assert
        assertTrue(deletedDeck.isEmpty());
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

    private void insertTestDeck(String deckName) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setString(1, deckName);
            preparedStatement.executeUpdate();
        }
    }

}