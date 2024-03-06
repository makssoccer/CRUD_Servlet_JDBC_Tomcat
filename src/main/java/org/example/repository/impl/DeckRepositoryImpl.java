package org.example.repository.impl;

import org.example.entity.Card;
import org.example.entity.Deck;
import org.example.entity.Player;
import org.example.repository.DeckRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeckRepositoryImpl implements DeckRepository {

    private final String INSERT_QUERY = "INSERT INTO deck (deck_name) VALUES (?)";
    private final String SELECT_ALL_QUERY = "SELECT * FROM deck";
    private final String SELECT_BY_ID_QUERY = "SELECT * FROM deck WHERE id = ?";
    private final String UPDATE_QUERY = "UPDATE deck SET deck_name = ? WHERE id = ?";
    private final String UPDATE_CARD_QUERY = "UPDATE card SET deck_id = ? WHERE id = ?";
    private final String DELETE_QUERY = "DELETE FROM deck WHERE id = ?";
    private DataSource dataSource;

    public DeckRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Deck> save(Deck deck) {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, deck.getDeckName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                deck.setId(resultSet.getInt(1));
                return Optional.of(deck);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Deck> findAll() {
        List<Deck> decks = new ArrayList<>();
        try (Statement statement = dataSource.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_QUERY)) {
            while (resultSet.next()) {
                decks.add(extractDeckFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return decks;
    }

    @Override
    public Optional<Deck> getById(Integer id) {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SELECT_BY_ID_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractDeckFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Deck> update(Deck deck) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            if (deck.getCards() != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CARD_QUERY)) {
                    for (Card card : deck.getCards()) {
                        preparedStatement.setInt(1, deck.getId());
                        preparedStatement.setInt(2, card.getId());
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                }
                deck = getCardsEntity(deck);
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
                preparedStatement.setString(1, deck.getDeckName());
                preparedStatement.setInt(2, deck.getId());
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    connection.commit();
                    return Optional.of(deck);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
//            connection.rollback();
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

    private Deck extractDeckFromResultSet(ResultSet resultSet) throws SQLException {
        Deck deck = new Deck();
        deck.setId(resultSet.getInt("id"));
        deck.setDeckName(resultSet.getString("deck_name"));
        if(resultSet.getInt("player_id")!=0){
            deck.setPlayer(new Player(resultSet.getInt("player_id")));
        }
        return deck;
    }

    private Deck getCardsEntity(Deck deck) {
        CardRepositoryImpl cardRepository = new CardRepositoryImpl(dataSource);
        List<Card> cards = cardRepository.getCardsByDeckId(deck.getId());
        deck.setCards(cards);
        return deck;
    }
}