package org.example.repository.impl;

import org.example.entity.Card;
import org.example.repository.CardRepository;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CardRepositoryImpl implements CardRepository {

    private final String INSERT = "INSERT INTO card (card_name, power) VALUES (?,?)";
    private final String SELECT_ALL = "SELECT * FROM card";
    private final String SELECT_BY_ID = "SELECT * FROM card WHERE id = ?";
    private final String SELECT_BY_DECK_ID = "SELECT * FROM card WHERE deck_id = ?";
    private final String UPDATE = "UPDATE card SET card_name = ?, power = ? WHERE id = ?";
    private final String DELETE_BY_ID = "DELETE FROM card WHERE id = ?";
    private DataSource dataSource;

    public CardRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Card> save(Card card) {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, card.getCardName());
            preparedStatement.setInt(2, card.getPower());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                card.setId(resultSet.getInt(1));
                return Optional.of(card);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Card> findAll() {
        List<Card> cards = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                cards.add(extractCardFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    @Override
    public Optional<Card> getById(Integer id) {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractCardFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Card> getCardsByDeckId(Integer deckId) {
        List<Card> cards = new ArrayList<>();
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SELECT_BY_DECK_ID)) {
            preparedStatement.setInt(1, deckId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    cards.add(extractCardFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    @Override
    public Optional<Card> update(Card card) {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(UPDATE)) {
            preparedStatement.setString(1, card.getCardName());
            preparedStatement.setInt(2, card.getPower());
            preparedStatement.setInt(3, card.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                return Optional.of(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void delete(Integer id) {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Card extractCardFromResultSet(ResultSet resultSet) throws SQLException {
        Card card = new Card();
        card.setId(resultSet.getInt("id"));
        card.setCardName(resultSet.getString("card_name"));
        card.setPower(resultSet.getInt("power"));
        return card;
    }
}
