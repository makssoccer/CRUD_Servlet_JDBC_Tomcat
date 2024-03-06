package CollectibleCardGame.integration;

import org.example.configuration.DataSourceCofig;
import org.example.entity.Player;
import org.example.repository.impl.PlayerRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class PlayerRepositoryImplIntegrationTest {

    private PlayerRepositoryImpl playerRepository;
    private DataSource dataSource;
    private Connection connection;
    private final String INSERT = "INSERT INTO player (name) VALUES (?)";
    private final String CREATE = "CREATE TABLE IF NOT EXISTS player (id SERIAL PRIMARY KEY, name VARCHAR(255), deck_id INT)";
    private final String DROP = "DROP TABLE IF EXISTS player";

    @BeforeEach
    void setUp() throws SQLException {
        dataSource = DataSourceCofig.getDataSource();
        playerRepository = new PlayerRepositoryImpl(dataSource);
        connection = dataSource.getConnection();
        dropTestTable();
        createTestTable();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void save_ShouldSavePlayer_WhenValidPlayerPassed() {
        // Arrange
        Player player = new Player(1,"Test Player");

        // Act
        Optional<Player> savedPlayer = playerRepository.save(player);

        // Assert
        assertAll(() -> assertTrue(savedPlayer.isPresent()),
                () ->assertEquals(player.getName(), savedPlayer.get().getName()),
                () ->assertNotNull(savedPlayer.get().getId()));
    }

    @Test
    void findAll_ShouldReturnAllPlayers() throws SQLException {
        // Arrange

        insertTestPlayer("Test Player 1");
        insertTestPlayer("Test Player 2");
        insertTestPlayer("Test Player 3");

        // Act
        List<Player> players = playerRepository.findAll();

        // Assert
        assertEquals(3, players.size());
    }

    @Test
    void getById_ShouldReturnPlayer_WhenValidIdPassed() throws SQLException {
        // Arrange
        Player player = new Player(1,"Test Player");
        insertTestPlayer("Test Player");

        // Act
        Optional<Player> retrievedPlayer = playerRepository.getById(player.getId());

        // Assert
        assertAll(() -> assertTrue(retrievedPlayer.isPresent()),
                ()-> assertEquals(player.getName(), retrievedPlayer.get().getName()));
    }

    @Test
    void update_ShouldUpdatePlayer_WhenValidPlayerPassed() throws SQLException {
        // Arrange
        Player player = new Player(1,"Test Player");
        insertTestPlayer("Test Player");
        player.setName("Updated Player");

        // Act
        Optional<Player> updatedPlayer = playerRepository.update(player);

        // Assert
        assertAll(() -> assertTrue(updatedPlayer.isPresent()),
                () -> assertEquals(player.getId(), updatedPlayer.get().getId()),
                () -> assertEquals(player.getName(), updatedPlayer.get().getName()));
    }

    @Test
    void delete_ShouldDeletePlayer_WhenValidIdPassed() throws SQLException {
        // Arrange
        Player player = new Player(1,"Test Player");
        insertTestPlayer("Test Player");

        // Act
        playerRepository.delete(player.getId());
        Optional<Player> deletedPlayer = playerRepository.getById(player.getId());

        // Assert
        assertTrue(deletedPlayer.isEmpty());
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

    private void insertTestPlayer( String playerName) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT , Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, playerName);
            preparedStatement.executeUpdate();
        }

    }
}
