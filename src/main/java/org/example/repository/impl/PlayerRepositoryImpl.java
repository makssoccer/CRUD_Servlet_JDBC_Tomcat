package org.example.repository.impl;

import org.example.entity.Deck;
import org.example.entity.Player;
import org.example.repository.DeckRepository;
import org.example.repository.PlayerRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerRepositoryImpl implements PlayerRepository {

    private final String INSERT_QUERY = "INSERT INTO player (name) VALUES (?)";
    private final String SELECT_ALL_QUERY = "SELECT * FROM player";
    private final String SELECT_BY_ID_QUERY = "SELECT * FROM player WHERE id = ?";
    private final String UPDATE_QUERY = "UPDATE player SET name = ?, deck_id = ? WHERE id = ?";
    private final String UPDATE_DECK_QUERY = "UPDATE deck SET player_id = ? WHERE id = ?";
    private final String DELETE_QUERY = "DELETE FROM player WHERE id = ?";
    private DataSource dataSource;

    public PlayerRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Player> save(Player player) {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, player.getName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                player.setId(resultSet.getInt(1));
                return Optional.of(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Player> findAll() {
        List<Player> players = new ArrayList<>();
        try (Statement statement = dataSource.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_QUERY)) {
            while (resultSet.next()) {
                players.add(extractPlayerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    @Override
    public Optional<Player> getById(Integer id) {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SELECT_BY_ID_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractPlayerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Player> update(Player player) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            if (player.getDeck()!= null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DECK_QUERY)) {
                    preparedStatement.setInt(1, player.getId());
                    preparedStatement.setInt(2, player.getDeck().getId());
                    preparedStatement.executeUpdate();
                }
                player = getDeckEntity(player);
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
                preparedStatement.setString(1, player.getName());
                // Если id deck нет, то в БД сохраняется 0
                preparedStatement.setInt(2, player.getDeck() == null ? 0 : player.getDeck().getId() );
                preparedStatement.setInt(3, player.getId());
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    connection.commit();
                    return Optional.of(player);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void delete(Integer id) {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Player extractPlayerFromResultSet(ResultSet resultSet) throws SQLException {
        Player player = new Player();
        player.setId(resultSet.getInt("id"));
        player.setName(resultSet.getString("name"));
        player = getDeckEntity(player);
        return player;
    }

    private Player getDeckEntity(Player player) {
        DeckRepository deckRepository = new DeckRepositoryImpl(dataSource);
        Optional<Deck> deckOptional = deckRepository.getById(player.getId());
        deckOptional.ifPresent(player::setDeck);
        return player;
    }
}