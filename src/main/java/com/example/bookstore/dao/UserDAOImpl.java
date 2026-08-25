package com.example.bookstore.dao;

import com.example.bookstore.db.DatabaseConnection;
import com.example.bookstore.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UserDAOImpl implements UserDAO {

    private static final String INSERT_SQL =
            "INSERT INTO users (name, email, password, role, is_active) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT id_user, name, email, password, role, is_active FROM users WHERE id_user = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT id_user, name, email, password, role, is_active FROM users ORDER BY id_user";
    private static final String UPDATE_SQL =
            "UPDATE users SET name = ?, email = ?, password = ?, role = ?, is_active = ? WHERE id_user = ?";
    private static final String DELETE_SQL =
            "DELETE FROM users WHERE id_user = ?";

    @Override
    public User create(User user) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setInsertOrUpdateParams(ps, user);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setIdUser(keys.getLong(1));
                }
            }
            return user;
        }
    }

    @Override
    public Optional<User> findById(long idUser) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setLong(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapRow(rs));
            }
        }
        return users;
    }

    @Override
    public boolean update(User user) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            setInsertOrUpdateParams(ps, user);
            ps.setLong(6, user.getIdUser());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(long idUser) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setLong(1, idUser);
            return ps.executeUpdate() > 0;
        }
    }

    private void setInsertOrUpdateParams(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getRole().name());
        ps.setBoolean(5, user.isActive());
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setIdUser(rs.getLong("id_user"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(User.Role.valueOf(rs.getString("role")));
        user.setActive(rs.getBoolean("is_active"));
        return user;
    }
}
